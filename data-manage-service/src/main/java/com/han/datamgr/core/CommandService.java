package com.han.datamgr.core;

import com.han.datamgr.exception.BusException;
import com.han.datamgr.vo.CommandVO;
import org.springframework.stereotype.Service;

/**
 * @author: Hanl
 * @date :2019/9/29
 * @desc: 命令服务
 */
@Service
public interface CommandService {

    /**
     * 创建命令
     *
     * @param commandVO
     */
    public void createCommand(CommandVO commandVO) throws BusException;


    /**
     * 更新命令
     *
     * @param commandVO
     */
    public void updateCommand(CommandVO commandVO) throws BusException;


    /**
     * 移除命令
     *
     * @param commandVO
     */
    public void removeCommand(CommandVO commandVO) throws BusException;
}
