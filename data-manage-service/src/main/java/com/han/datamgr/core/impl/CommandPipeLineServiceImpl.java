package com.han.datamgr.core.impl;


import com.alibaba.fastjson.parser.ParserConfig;
import com.han.datamgr.core.CommandPipeLineService;
import com.han.datamgr.core.FlowLineService;
import com.han.datamgr.entity.*;
import com.han.datamgr.exception.BusException;
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

    @Override
    public CommandPipeline buildCommandPipeline(String DataProcessFlowId) throws BusException {
        Optional<DataProcessFlowEntity> optional = dataProcessFlowRepository.findById(DataProcessFlowId);
        if (!optional.isPresent()) {
            throw new BusException("没有找到对应的数据流程,id=" + DataProcessFlowId);
        }
        DataProcessFlowEntity flowEntity = optional.get();
        Map<String, CanvasCommandInstanceEntity> startAndEnd = flowLineService.findStartAndEndCmd(DataProcessFlowId);
        String start = startAndEnd.get(FlowLineService.START_CMD).getId();
        String end = startAndEnd.get(FlowLineService.END_CMD).getId();
        Set<FlowLineEntity> flowLineEntitySet = flowEntity.getFlowLineSet();

        CommandPipeline commandPipeline = buildCommandPipe(FlowUtils.fromFlowLineEntity(flowLineEntitySet), start, end,
                FlowUtils.fromFlowLineEntityToNodeList(flowLineEntitySet), flowEntity.getDataProcessFlowName());

        return commandPipeline;
    }

    /**
     * @param flowLine
     * @param start
     * @param end
     * @param nodeList
     */
    private CommandPipeline buildCommandPipe(List<Map<String, String>> flowLine, String start, String end, List<CanvasCommandInstanceEntity> nodeList, String flowNme) throws BusException {
        //先计算主流程的命令构建------------------
        List<String> mainFlow = FlowUtils.findFlow(flowLine, start, end);
        Map<String, CanvasCommandInstanceEntity> idInstance = new HashMap<>();
        nodeList.forEach(node -> {
            idInstance.put(node.getId(), node);
        });

        CommandPipeline commandPipeline = CommandPipeline.build(flowNme);
        for (String id : mainFlow) {
            //-------------需要计算每个节点子流程命令构建
            Set<CommandInstanceFlowRelation> cmdInstanceFowRelSet = idInstance.get(id).getCommandInstanceEntity().getCmdInstanceFowRelSet();
            List<Map<String, Object>> subPipMapList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(cmdInstanceFowRelSet)) {
                for (CommandInstanceFlowRelation relation : cmdInstanceFowRelSet) {
                    Set<FlowLineEntity> flowLineEntitySet = relation.getFlowEntity().getFlowLineSet();
                    String subFlowId = relation.getFlowEntity().getId();
                    String subFlowName = relation.getFlowEntity().getDataProcessFlowName();
                    Map<String, CanvasCommandInstanceEntity> startAndEnd = flowLineService.findStartAndEndCmd(subFlowId);
                    //---------------------获取子流程的执行顺序
                    List<Map<String, String>> subFlowLineMap = FlowUtils.fromFlowLineEntityToId(flowLineEntitySet);
                    String start0 = startAndEnd.get(FlowLineService.START_CMD).getId();
                    String end0 = startAndEnd.get(FlowLineService.END_CMD).getId();
                    //---------------------递归调用
                    CommandPipeline subPipe = buildCommandPipe(subFlowLineMap, start0, end0, nodeList, subFlowName);
                    log.info("create sub pipe={}", subPipe.get());
                    subPipMapList.add(subPipe.get());
                }
            }
            Map<String, Object> cmdMap = buildCommandMapByConfig(idInstance.get(id).getCommandInstanceEntity(), commandPipeline, subPipMapList);
            commandPipeline.addCommand(cmdMap);
        }

        return commandPipeline;
    }

    public Map<String, Object> buildCommandMapByConfig(CommandInstanceEntity commandInstanceEntity, CommandPipeline commandPipeline, List<Map<String, Object>> subPipMapList) throws BusException {
        Set<CommandParamEntity> commandParamEntityList = commandInstanceEntity.getCmdInstanceParams();
        Map<String, Object> result = new HashMap<>();
        for (CommandParamEntity commandParamEntity : commandParamEntityList) {
            String key = commandParamEntity.getFieldName();
            if (null == key || "".equals(key)) {
                throw new BusException("Command初始化参数名称不能为空.");
            }

            String valueString = commandParamEntity.getFiledValue();
            Object value = TypeUtils.fastJsonCast(valueString, commandParamEntity.getFieldType(), new ParserConfig());
            if ("importCommands".equals(key)) {
                commandPipeline.addImports((List<String>) value);
                continue;
            }
            result.put(commandParamEntity.getFieldName(), value);
        }
        //-------------------构建节点的子流程
        result.put(AbstractCommand.SUB_FLOW_KEY, subPipMapList);
        result.put(AbstractCommand.COMMAND_INSTANCE_ID, commandInstanceEntity.getId());
        result.put(AbstractCommand.SUB_FLOW_SELECTOR_KEY, commandInstanceEntity.getCommand().getSubFlowSelectorClazz() == null ? "" : commandInstanceEntity.getCommand().getSubFlowSelectorClazz());
        Map<String, Object> resultCommand = new HashMap<>();
        String morphName = commandInstanceEntity.getCommand().getCommandMorphName();
        resultCommand.put(morphName, result);
        return resultCommand;
    }
}
