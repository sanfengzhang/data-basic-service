package com.han.datamgr.core.impl;

import com.han.datamgr.core.DataProcessFlowService;
import com.han.datamgr.exception.BusException;
import com.han.datamgr.vo.DataProcessFlowVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: Hanl
 * @date :2019/9/29
 * @desc:
 */
@Service
public class DataProcessFlowServiceImpl implements DataProcessFlowService {

    @Override
    public List<DataProcessFlowVO> queryDataProcessFlows() throws BusException {
        return null;
    }

    @Override
    public void createDataProcessFlow(DataProcessFlowVO dataProcessFlowVO) throws BusException {

    }

    @Override
    public void updateDataProcessFlowCommandDetail(DataProcessFlowVO dataProcessFlowVO) throws BusException {

    }

    @Override
    public void removeDataProcessFlow(DataProcessFlowVO dataProcessFlowVO) throws BusException {

    }


}
