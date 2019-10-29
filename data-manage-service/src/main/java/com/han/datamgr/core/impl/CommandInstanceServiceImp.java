package com.han.datamgr.core.impl;

import com.han.datamgr.core.CommandInstanceService;
import com.han.datamgr.entity.*;
import com.han.datamgr.exception.BusException;
import com.han.datamgr.repository.*;
import com.han.datamgr.vo.CommandInstanceVO;
import com.han.datamgr.vo.CommandVO;
import com.han.datamgr.vo.LeftMenuVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @author: Hanl
 * @date :2019/9/30
 * @desc:
 */
@Service
@Slf4j
public class CommandInstanceServiceImp implements CommandInstanceService {

    @Autowired
    private CommandInstanceRepository commandInstanceRepository;

    @Autowired
    private CommandParamRepository commandParamRepository;

    @Autowired
    private CommandInstanceParamRepository commandInstanceParamRepository;

    @Autowired
    private DataProcessFlowRepository flowRepository;

    @Autowired
    private CommandRepository commandRepository;

    @Autowired
    private CommandInstanceFlowRelationRepository commandInstanceFlowRelationRepository;

    @Override
    @Transactional
    public void createCmdInstance(CommandInstanceVO vo) throws BusException {
        CommandInstanceEntity entity = new CommandInstanceEntity();
        entity.setCommandInstanceName(vo.getCommandInstanceName());
        entity.setSelectSubFlowClazz(vo.getSelectSubFlowClazz());
        String commandId = vo.getCommand();
        CommandEntity commandEntity = commandRepository.findById(commandId).get();
        entity.setCommand(commandEntity);
        List<CommandParamEntity> commandParamEntityList = vo.getCmdParams();
        List<DataProcessFlowEntity> flowEntities = flowRepository.findAllById(vo.getSubFlows());

        entity = commandInstanceRepository.save(entity);
        List<CommandInstanceFlowRelation> relations = new ArrayList<>();
        for (DataProcessFlowEntity flowEntity : flowEntities) {
            CommandInstanceFlowRelation relation = new CommandInstanceFlowRelation();
            relation.setFlowEntity(flowEntity);
            relation.setCommandInstanceEntity(entity);
            relations.add(relation);
        }
        commandInstanceFlowRelationRepository.saveAll(relations);

        List<CommandInstanceParamEntity> commandInstanceParamEntities = new ArrayList<>();
        for (CommandParamEntity commandParamEntity : commandParamEntityList) {
            Optional<CommandParamEntity> optionalCommandParamEntity = commandParamRepository.findById(commandParamEntity.getId());
            CommandParamEntity commandParamEntity1 = optionalCommandParamEntity.get();

            CommandInstanceParamEntity commandInstanceParamEntity = new CommandInstanceParamEntity();
            commandInstanceParamEntity.setFieldName(commandParamEntity1.getFieldName());
            commandInstanceParamEntity.setFieldType(commandParamEntity1.getFieldType());
            commandInstanceParamEntity.setFieldValue(commandParamEntity.getFieldValue());
            commandInstanceParamEntity.setCommandParamEntity(commandParamEntity1);
            commandInstanceParamEntity.setCommandInstanceEntity(entity);
            commandInstanceParamEntities.add(commandInstanceParamEntity);
        }
        commandInstanceParamRepository.saveAll(commandInstanceParamEntities);
    }

    @Override
    public void updateCmdInstance(CommandInstanceVO commandInstanceVO) throws BusException {

    }

    @Override
    public void removeCmdInstance(CommandInstanceVO commandInstanceVO) throws BusException {

    }
}
