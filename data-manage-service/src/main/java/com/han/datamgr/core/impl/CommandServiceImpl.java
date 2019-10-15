package com.han.datamgr.core.impl;

import com.han.datamgr.core.CommandService;
import com.han.datamgr.entity.CommandEntity;
import com.han.datamgr.entity.CommandInstanceEntity;
import com.han.datamgr.exception.BusException;
import com.han.datamgr.repository.CommandRepository;
import com.han.datamgr.support.DataTransformUtils;
import com.han.datamgr.vo.CommandInstanceVO;
import com.han.datamgr.vo.CommandVO;
import com.han.datamgr.vo.LeftMenuVO;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
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
    @Transactional
    public List<LeftMenuVO> getLeftMenuCmdInstanceData() throws BusException {
        List<LeftMenuVO> result = new ArrayList<>();
        List<CommandEntity> allCmd = commandRepository.findAll();
        Map<String, List<CommandEntity>> map = null;
        if (!CollectionUtils.isEmpty(allCmd)) {
            DataTransformUtils<String, List<CommandEntity>> dataTransformUtils = new DataTransformUtils<>();
            try {
                map = dataTransformUtils.convert("commandType", allCmd.iterator());
            } catch (IllegalAccessException e) {
                throw new BusException("", e);
            }
            if (null != map) {
                Iterator<Map.Entry<String, List<CommandEntity>>> it = map.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, List<CommandEntity>> en = it.next();
                    LeftMenuVO leftMenuVO = new LeftMenuVO();
                    leftMenuVO.setType("group");
                    leftMenuVO.setName(en.getKey());
                    leftMenuVO.setIco("'el-icon-video-play'");
                    List<CommandEntity> commandEntities=en.getValue();
                    List<CommandInstanceVO> res=new ArrayList<>();
                    commandEntities.forEach(commandEntity -> {
                        commandEntity.getCommandInstanceEntityList().forEach(commandInstance->{
                            CommandInstanceVO commandInstanceVO=new CommandInstanceVO();
                            commandInstanceVO.from(commandInstance);
                            res.add(commandInstanceVO);
                        });
                    });
                    leftMenuVO.setChildren(res);
                    result.add(leftMenuVO);
                }
            }
        }
        return result;
    }


    @Override
    public void createCommand(CommandVO commandVO) throws BusException {
        CommandEntity commandEntity = commandVO.to();
        commandRepository.save(commandEntity);
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
