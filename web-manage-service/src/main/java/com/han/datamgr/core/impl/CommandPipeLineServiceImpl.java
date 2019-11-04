package com.han.datamgr.core.impl;


import com.alibaba.fastjson.parser.ParserConfig;
import com.han.datamgr.core.CommandPipeLineService;
import com.han.datamgr.core.FlowLineService;
import com.han.datamgr.entity.*;
import com.han.datamgr.exception.BusException;
import com.han.datamgr.repository.CanvasCommandInstanceRepository;
import com.han.datamgr.repository.DataProcessFlowRepository;
import com.han.datamgr.utils.FlowUtils;
import com.stream.data.transform.model.CommandPipeline;
import com.stream.data.transform.utils.TypeUtils;
import lombok.extern.slf4j.Slf4j;
import org.kitesdk.morphline.base.AbstractCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @author: Hanl
 * @date :2019/10/15
 * @desc:
 */
@Service
@Slf4j
public class CommandPipeLineServiceImpl implements CommandPipeLineService {

    @Autowired
    private DataProcessFlowRepository dataProcessFlowRepository;

    @Autowired
    private FlowLineService flowLineService;

    @Autowired
    private CanvasCommandInstanceRepository canvasCommandInstanceRepository;

    @Override
    public List<CommandPipeline> buildCommandPipeline(String DataProcessFlowId) throws BusException {
        Optional<DataProcessFlowEntity> optional = dataProcessFlowRepository.findById(DataProcessFlowId);
        if (!optional.isPresent()) {
            throw new BusException("没有找到对应的数据流程,id=" + DataProcessFlowId);
        }
        //-----------------------获取所有需要初始化的流程--------
        DataProcessFlowEntity flowEntity = optional.get();
        String flowNme = flowEntity.getDataProcessFlowName();
        List<String> flowNameSet = new ArrayList<>();
        findAllSubFlowName(flowNme, flowNameSet);
        flowNameSet.add(flowNme);//将原始的流程加入到集合中去
        findAllBranchFlowName(flowNme,flowNameSet);


        List<CommandPipeline> result = new ArrayList<>();
        for (String flowName : flowNameSet) {
            DataProcessFlowEntity flowEntity1 = dataProcessFlowRepository.findByDataProcessFlowName(flowName).get();
            CommandPipeline commandPipeline = buildCommandPipe(flowEntity1);
            result.add(commandPipeline);
        }
        return result;
    }

    //-----------------找出该流程包含的所有子流程----
    public void findAllSubFlowName(String DataProcessFlowName, List<String> flowNameSet) throws BusException {
        Optional<DataProcessFlowEntity> optional = dataProcessFlowRepository.findByDataProcessFlowName(DataProcessFlowName);
        if (!optional.isPresent()) {
            throw new BusException("没有找到对应的数据流程,flowName=" + DataProcessFlowName);
        }
        DataProcessFlowEntity flowEntity = optional.get();
        Set<FlowLineEntity> flowLineEntitySet = flowEntity.getFlowLineSet();
        List<CanvasCommandInstanceEntity> nodeList = FlowUtils.fromFlowLineEntityToNodeList(flowLineEntitySet);
        for (CanvasCommandInstanceEntity node : nodeList) {
            String morphName = node.getCommandInstanceEntity().getCommand().getCommandMorphName();
            if ("callSubPipe".equals(morphName)) {
                Set<CommandInstanceParamEntity> instanceParamEntities = node.getCommandInstanceEntity().getCmdInstanceParams();
                for (CommandInstanceParamEntity instanceParamEntity : instanceParamEntities) {
                    if ("flowId".equals(instanceParamEntity.getFieldName())) {
                        String flowName = instanceParamEntity.getFieldValue();
                        if (!flowNameSet.contains(flowName)) {//----在集合中不存在的就加入,否则已经存在了就不需要递归查询其包含的子流程
                            flowNameSet.add(flowName);
                            findAllSubFlowName(flowName, flowNameSet);
                        }
                    }
                }
            }
        }
    }

    public void findAllBranchFlowName(String DataProcessFlowName, List<String> flowNameSet) throws BusException {
        Optional<DataProcessFlowEntity> optional = dataProcessFlowRepository.findByDataProcessFlowName(DataProcessFlowName);
        if (!optional.isPresent()) {
            throw new BusException("没有找到对应的数据流程,flowName=" + DataProcessFlowName);
        }
        DataProcessFlowEntity flowEntity = optional.get();
        Set<FlowLineEntity> flowLineEntitySet = flowEntity.getFlowLineSet();
        List<CanvasCommandInstanceEntity> nodeList = FlowUtils.fromFlowLineEntityToNodeList(flowLineEntitySet);
        for (CanvasCommandInstanceEntity node : nodeList) {
            String morphName = node.getCommandInstanceEntity().getCommand().getCommandMorphName();
            if ("branchPipe".equals(morphName)) {
                Set<CommandInstanceParamEntity> instanceParamEntities = node.getCommandInstanceEntity().getCmdInstanceParams();
                for (CommandInstanceParamEntity instanceParamEntity : instanceParamEntities) {
                    if ("branchFlowIds".equals(instanceParamEntity.getFieldName())) {
                        List<String> flowNames = (List<String>) TypeUtils.fastJsonCast(instanceParamEntity.getFieldValue(),
                                instanceParamEntity.getFieldType(), new ParserConfig());
                        for (String flowName : flowNames) {
                            if (!flowNameSet.contains(flowName)) {//----在集合中不存在的就加入,否则已经存在了就不需要递归查询其包含的子流程
                                flowNameSet.add(flowName);
                                findAllSubFlowName(flowName, flowNameSet);
                            }
                        }
                    }
                }
            }
        }
    }


    /**
     * @param flowEntity
     * @return
     * @throws BusException
     */
    private CommandPipeline buildCommandPipe(DataProcessFlowEntity flowEntity) throws BusException {
        //先计算主流程的命令构建------------------
        Map<String, CanvasCommandInstanceEntity> startAndEnd = flowLineService.findStartAndEndCmd(flowEntity.getId());

        String start = startAndEnd.get(FlowLineService.START_CMD).getId();
        String end = startAndEnd.get(FlowLineService.END_CMD) == null ? null : startAndEnd.get(FlowLineService.END_CMD).getId();
        Set<FlowLineEntity> flowLineEntitySet = flowEntity.getFlowLineSet();
        List<Map<String, String>> flowLine = FlowUtils.fromFlowLineEntityToId(flowLineEntitySet);
        List<String> mainFlowIdsOrder = FlowUtils.findMainFlowLine(flowLine, start, end);

        CommandPipeline commandPipeline = CommandPipeline.build(flowEntity.getDataProcessFlowName());
        for (String canvasCommandInstanceEntityId : mainFlowIdsOrder) {
            commandPipeline.addCommand(buildCommandMapByConfig(canvasCommandInstanceRepository.getOne(canvasCommandInstanceEntityId).getCommandInstanceEntity(), commandPipeline));
        }
        return commandPipeline;
    }


    public Map<String, Object> buildCommandMapByConfig(CommandInstanceEntity commandInstanceEntity, CommandPipeline commandPipeline) throws BusException {
        Set<CommandInstanceParamEntity> commandInstanceParamEntityList = commandInstanceEntity.getCmdInstanceParams();
        Map<String, Object> result = new HashMap<>();
        for (CommandInstanceParamEntity commandInstanceParamEntity : commandInstanceParamEntityList) {
            String key = commandInstanceParamEntity.getFieldName();
            if (null == key || "".equals(key)) {
                throw new BusException("Command初始化参数名称不能为空.");
            }

            String valueString = commandInstanceParamEntity.getFieldValue();
            Object value = TypeUtils.fastJsonCast(valueString, commandInstanceParamEntity.getFieldType(), new ParserConfig());
            if ("importCommands".equals(key)) {
                commandPipeline.addImports((List<String>) value);
                continue;
            }
            result.put(commandInstanceParamEntity.getFieldName(), value);
        }
        //-------------------构建节点的子流程
        result.put(AbstractCommand.COMMAND_INSTANCE_ID, commandInstanceEntity.getId());
        Map<String, Object> resultCommand = new HashMap<>();
        String morphName = commandInstanceEntity.getCommand().getCommandMorphName();
        resultCommand.put(morphName, result);
        return resultCommand;
    }
}
