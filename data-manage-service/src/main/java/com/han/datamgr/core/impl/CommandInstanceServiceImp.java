package com.han.datamgr.core.impl;

import com.han.datamgr.core.CommandInstanceService;
import com.han.datamgr.entity.CommandInstanceEntity;
import com.han.datamgr.exception.BusException;
import com.han.datamgr.repository.CommandInstanceRepository;
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

    @Override
    public List<LeftMenuVO> getLeftMenuCmdInstanceData() throws BusException {
        List<LeftMenuVO> result = new ArrayList<>();
        List<CommandInstanceEntity> allCmdInstances = commandInstanceRepository.findAll();
        Map<String, List<CommandInstanceVO>> typeToCmdList = new HashMap<>();
        if (!CollectionUtils.isEmpty(allCmdInstances)) {
            allCmdInstances.forEach(cmd -> {
                String type = cmd.getCommand().getCommandType();
                List<CommandInstanceVO> cmdList = typeToCmdList.get(type);
                if (null == cmdList) {
                    cmdList = new ArrayList<>();
                    typeToCmdList.put(type, cmdList);
                }
                CommandInstanceVO vo = new CommandInstanceVO();
                vo.from(cmd);
                cmdList.add(vo);
            });
            Iterator<Map.Entry<String, List<CommandInstanceVO>>> it = typeToCmdList.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, List<CommandInstanceVO>> en = it.next();
                LeftMenuVO leftMenuVO = new LeftMenuVO();
                leftMenuVO.setType("group");
                leftMenuVO.setName(en.getKey());
                leftMenuVO.setIco("'el-icon-video-play'");
                leftMenuVO.setChildren(en.getValue());
                result.add(leftMenuVO);
            }
        }
        return result;
    }


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
