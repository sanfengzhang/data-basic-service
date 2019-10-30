package com.han.datamgr.core.impl;

import com.han.datamgr.core.CommandService;
import com.han.datamgr.entity.CommandEntity;
import com.han.datamgr.entity.CommandParamEntity;
import com.han.datamgr.exception.BusException;
import com.han.datamgr.repository.CommandRepository;
import com.han.datamgr.vo.CommandVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

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
