package com.han.datamgr.core.impl;

import com.han.datamgr.core.DataProcessFlowService;
import com.han.datamgr.entity.CommandInstanceEntity;
import com.han.datamgr.entity.DataProcessFlowCmdInstanceRelation;
import com.han.datamgr.entity.DataProcessFlowEntity;
import com.han.datamgr.exception.BusException;
import com.han.datamgr.repository.DataProcessFlowRepository;
import com.han.datamgr.vo.DataProcessFlowVO;
import com.stream.data.transform.model.CommandPipeline;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author: Hanl
 * @date :2019/9/29
 * @desc:
 */
@Service
public class DataProcessFlowServiceImpl implements DataProcessFlowService {

    @Override
    public List<DataProcessFlowVO> queryDataProcessFlows(DataProcessFlowVO queryParams) throws BusException {
        if (null == queryParams) {

        }
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
