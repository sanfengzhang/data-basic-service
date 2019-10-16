package com.han.datamgr.core.impl;


import com.alibaba.fastjson.parser.ParserConfig;
import com.han.datamgr.core.CommandPipeLineService;
import com.han.datamgr.entity.CommandInstanceEntity;
import com.han.datamgr.entity.CommandParamEntity;
import com.han.datamgr.entity.DataProcessFlowCmdInstanceRelation;
import com.han.datamgr.entity.DataProcessFlowEntity;
import com.han.datamgr.exception.BusException;
import com.han.datamgr.repository.DataProcessFlowRepository;
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
        DataProcessFlowEntity dataProcessFlowEntity = optional.get();
        List<DataProcessFlowCmdInstanceRelation> dataProcessFlowCmdInstanceRelations = dataProcessFlowEntity.getCmdInstanceEntityList();
        CommandPipeline commandPipeline = new CommandPipeline(dataProcessFlowEntity.getDataProcessFlowName());
        if (!CollectionUtils.isEmpty(dataProcessFlowCmdInstanceRelations)) {
            //------------------------------创建命令流
            for (DataProcessFlowCmdInstanceRelation relation : dataProcessFlowCmdInstanceRelations) {
                CommandInstanceEntity commandInstanceEntity = relation.getCommandInstanceEntity();
                Map<String, Object> commandMap = buildCommandMapByConfig(commandInstanceEntity, commandPipeline);
                Map<String, Object> morphCommandMap = new HashMap<>();
                morphCommandMap.put(commandInstanceEntity.getCommand().getCommandMorphName(), commandMap);
                commandPipeline.addCommand(morphCommandMap);
            }
        }
        return commandPipeline;
    }

    public Map<String, Object> buildCommandMapByConfig(CommandInstanceEntity commandInstanceEntity, CommandPipeline commandPipeline) throws BusException {
        List<CommandParamEntity> commandParamEntityList = commandInstanceEntity.getCmdInstanceParams();
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
