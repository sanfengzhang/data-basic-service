package com.han.datamgr.core;

import com.han.datamgr.entity.CanvasCommandInstanceEntity;
import com.han.datamgr.entity.CommandInstanceEntity;

import java.util.Map;

/**
 * @author: Hanl
 * @date :2019/10/23
 * @desc:
 */
public interface FlowLineService {

    public static final String START_CMD="from";

    public static final String END_CMD="to";

    public Map<String, CanvasCommandInstanceEntity> findStartAndEndCmd(String flowId);
}
