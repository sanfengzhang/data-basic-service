package com.han.stream.flink;

import com.han.dataflow.api.model.AbstractDataProcessNode;
import com.han.stream.BuildJobService;

import java.util.List;
import java.util.Map;

/**
 * @author: Hanl
 * @date :2019/8/7
 * @desc:
 */
public class FlinkBuildJobService implements BuildJobService {

    @Override
    public void buildJobFromDataProcessFlow(List<List<AbstractDataProcessNode>> flows, Map<String,Object> jobParamters) {


    }
}
