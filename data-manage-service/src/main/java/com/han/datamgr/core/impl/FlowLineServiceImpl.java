package com.han.datamgr.core.impl;

import com.han.datamgr.core.FlowLineService;
import com.han.datamgr.entity.CanvasCommandInstanceEntity;
import com.han.datamgr.entity.CommandInstanceEntity;
import com.han.datamgr.entity.FlowLineEntity;
import com.han.datamgr.repository.FlowLineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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
    public Map<String, CanvasCommandInstanceEntity> findStartAndEndCmd(String flowId) {
        List<FlowLineEntity> flowLineEntities = flowLineRepository.findAllByFlowEntity_Id(flowId);
        Map<String, CanvasCommandInstanceEntity> result = new HashMap<>();
        if (!CollectionUtils.isEmpty(flowLineEntities)) {
            for (FlowLineEntity flowLineEntity : flowLineEntities) {
                CanvasCommandInstanceEntity start = flowLineEntity.getStart();
                CanvasCommandInstanceEntity end = flowLineEntity.getEnd();
                if (start.isFirst()) {
                    result.put(START_CMD, start);
                }
                if (end.isLast()) {
                    result.put(END_CMD, end);
                }
            }

        }
        return result;
    }
}
