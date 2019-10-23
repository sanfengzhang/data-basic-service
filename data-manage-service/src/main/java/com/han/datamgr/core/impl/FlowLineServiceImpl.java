package com.han.datamgr.core.impl;

import com.han.datamgr.core.FlowLineService;
import com.han.datamgr.entity.CommandInstanceEntity;
import com.han.datamgr.entity.FlowLineEntity;
import com.han.datamgr.repository.FlowLineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Hanl
 * @date :2019/10/23
 * @desc:
 */
@Service
public class FlowLineServiceImpl implements FlowLineService {

    @Autowired
    private FlowLineRepository flowLineRepository;

    @Override
    public Map<String, CommandInstanceEntity> findStartAndEndCmd(String flowId) {
        List<FlowLineEntity> flowLineEntities = flowLineRepository.queryFlowLineByLineStatus(flowId);
        Map<String, CommandInstanceEntity> result = new HashMap<>();
        result.put(START_CMD, flowLineEntities.get(0).getStart());
        if (null == flowLineEntities || flowLineEntities.size() == 0) {

            return result;
        }

        if (flowLineEntities.size() == 1) {
            result.put(END_CMD, flowLineEntities.get(0).getEnd());
        }

        if (flowLineEntities.size() == 2) {
            result.put(END_CMD, flowLineEntities.get(1).getEnd());
        }

        return result;
    }
}
