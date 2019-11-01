package com.han.datamgr.core;

import com.han.datamgr.exception.BusException;
import com.stream.data.transform.model.CommandPipeline;

import java.util.List;

/**
 * @author: Hanl
 * @date :2019/10/15
 * @desc:
 */
public interface CommandPipeLineService {

    /**
     * 通过数据流程获取对应流程的命令构建
     *
     * @param DataProcessFlowId
     * @return
     * @throws BusException
     */
    public List<CommandPipeline> buildCommandPipeline(String DataProcessFlowId) throws BusException;

}
