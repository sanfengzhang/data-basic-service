package com.han.datamgr.core.impl;


import com.alibaba.fastjson.parser.ParserConfig;
import com.google.common.collect.ArrayListMultimap;
import com.han.datamgr.core.CommandPipeLineService;
import com.han.datamgr.entity.CommandInstanceEntity;
import com.han.datamgr.entity.CommandParamEntity;
import com.han.datamgr.entity.DataProcessFlowEntity;
import com.han.datamgr.entity.FlowLineEntity;
import com.han.datamgr.exception.BusException;
import com.han.datamgr.repository.DataProcessFlowRepository;
import com.han.datamgr.utils.FlowUtils;
import com.han.datamgr.vo.DataProcessFlowVO;
import com.stream.data.transform.model.CommandPipeline;
import com.stream.data.transform.utils.TypeUtils;
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
public class CommandPipeLineServiceImpl implements CommandPipeLineService {

    @Autowired
    private DataProcessFlowRepository dataProcessFlowRepository;

    @Override
    public CommandPipeline createCommandPipeline(String DataProcessFlowId) throws BusException {
        Optional<DataProcessFlowEntity> optional = dataProcessFlowRepository.findById(DataProcessFlowId);
        if (!optional.isPresent()) {
            throw new BusException("没有找到对应的数据流程,id=" + DataProcessFlowId);
        }
        DataProcessFlowEntity flowEntity = optional.get();
        DataProcessFlowVO vo = new DataProcessFlowVO();
        vo.setFlowEntity(flowEntity);
        vo.formEntityToLineList();
        vo.fromEntityToNodeList();


        return null;
    }

    /**
     * @param flowLine
     */
    private void buildCommandPipe(List<Map<String, String>> flowLine, String start, String end) {
        Map<String, List<List<String>>> subFlowForNode = FlowUtils.findAllSubFlow(flowLine, start, end);
        //------------------不存在子流程
        if (CollectionUtils.isEmpty(subFlowForNode)) {

        }


    }


    private List<CommandInstanceEntity> findStartAndEndCmd(DataProcessFlowVO vo) {
        List<CommandInstanceEntity> cmdList = vo.getNodeList();
        List<CommandInstanceEntity> result = new ArrayList<>();
        for (CommandInstanceEntity cmd : cmdList) {
            if ("".equals(cmd.getCommand().getCommandType())) {
                cmdList.add(cmd);
            }
            if ("".equals(cmd.getCommand().getCommandType())) {
                cmdList.add(cmd);
            }
        }
        return cmdList;
    }

    public Map<String, Object> buildCommandMapByConfig(CommandInstanceEntity commandInstanceEntity, CommandPipeline commandPipeline) throws BusException {
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
        return result;
    }
}
