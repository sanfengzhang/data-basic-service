package com.han.stream;

import com.han.dataflow.api.model.AbstractDataProcessNode;

import java.util.List;
import java.util.Map;

/**
 * @author: Hanl
 * @date :2019/8/7
 * @desc:
 */
public interface BuildJobService {

    public void buildJobFromDataProcessFlow(List<List<AbstractDataProcessNode>> flows, Map<String,Object> jobParamters);

}
