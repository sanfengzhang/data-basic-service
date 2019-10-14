package com.han.datamgr.core;

import com.han.datamgr.entity.CommandInstanceEntity;
import com.han.datamgr.exception.BusException;
import com.han.datamgr.vo.CommandInstanceVO;
import com.han.datamgr.vo.LeftMenuVO;

import java.util.List;
import java.util.Map;

/**
 * @author: Hanl
 * @date :2019/9/30
 * @desc:
 */
public interface CommandInstanceService {

    public void createCmdInstance(CommandInstanceVO commandInstanceVO) throws BusException;

    public void updateCmdInstance(CommandInstanceVO commandInstanceVO) throws BusException;

    public void removeCmdInstance(CommandInstanceVO commandInstanceVO) throws BusException;

    public List<LeftMenuVO> getLeftMenuCmdInstanceData() throws BusException;

}
