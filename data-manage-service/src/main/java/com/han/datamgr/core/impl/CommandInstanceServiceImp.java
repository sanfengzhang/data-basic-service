package com.han.datamgr.core.impl;

import com.han.datamgr.core.CommandInstanceService;
import com.han.datamgr.entity.CommandInstanceEntity;
import com.han.datamgr.exception.BusException;
import com.han.datamgr.repository.CommandInstanceRepository;
import com.han.datamgr.vo.CommandInstanceVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author: Hanl
 * @date :2019/9/30
 * @desc:
 */
@Service
public class CommandInstanceServiceImp implements CommandInstanceService {

    @Autowired
    private CommandInstanceRepository commandInstanceRepository;

    @Override
    @Transactional
    public void createCmdInstance(CommandInstanceVO commandInstanceVO) throws BusException {

        CommandInstanceEntity commandInstanceEntity = commandInstanceVO.to();
        commandInstanceRepository.save(commandInstanceEntity);
    }

    @Override
    public void updateCmdInstance(CommandInstanceVO commandInstanceVO) throws BusException {

    }

    @Override
    public void removeCmdInstance(CommandInstanceVO commandInstanceVO) throws BusException {

    }
}
