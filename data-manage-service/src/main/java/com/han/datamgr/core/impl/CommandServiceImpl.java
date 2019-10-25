package com.han.datamgr.core.impl;

import com.han.datamgr.core.CommandService;
import com.han.datamgr.entity.CommandEntity;
import com.han.datamgr.entity.CommandInstanceEntity;
import com.han.datamgr.entity.CommandParamEntity;
import com.han.datamgr.exception.BusException;
import com.han.datamgr.repository.CommandRepository;
import com.han.datamgr.support.DataTransformUtils;
import com.han.datamgr.vo.CommandInstanceVO;
import com.han.datamgr.vo.CommandVO;
import com.han.datamgr.vo.LeftMenuVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @author: Hanl
 * @date :2019/10/5
 * @desc:
 */
@Service
public class CommandServiceImpl implements CommandService {

    @Autowired
    private CommandRepository commandRepository;

    @Override
    public void createCommand(CommandEntity entity) throws BusException {

        Set<CommandParamEntity> paramEntitySet = entity.getCmdParams();
        for (CommandParamEntity paramEntity : paramEntitySet) {
            paramEntity.setCommandEntity(entity);
        }
        commandRepository.save(entity);
    }

    @Override
    public void updateCommand(CommandVO commandVO) throws BusException {

    }

    @Override
    public void removeCommand(CommandVO commandVO) throws BusException {

    }

    public List<CommandEntity> queryAllCommands() throws BusException {

        return commandRepository.findAll();

    }
}
