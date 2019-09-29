package com.han.datamgr.common;

import com.han.datamgr.exception.EventException;
import com.han.datamgr.vo.EventVO;

/**
 * @author: Hanl
 * @date :2019/9/29
 * @desc:
 */
public interface EventListener {

    /**
     * 创建一个事件、根据EventVO获取事件相关的信息去做相关的处理
     * @param event
     * @return
     * @throws EventException
     */
    public EventVO listen(EventVO event) throws EventException;

}
