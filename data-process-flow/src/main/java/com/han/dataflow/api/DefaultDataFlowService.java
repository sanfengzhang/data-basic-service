package com.han.dataflow.api;

import com.han.dataflow.api.model.AbstractDataProcessNode;
import com.han.dataflow.api.model.DataProcessFlowModel;
import com.han.dataflow.api.model.DataProcessNodeEdge;
import com.han.dataflow.core.DataProcessFlowService;

import java.util.List;

/**
 * @author: Hanl
 * @date :2019/8/15
 * @desc:
 */
public class DefaultDataFlowService implements DataFlowService {

    @Override
    public DataProcessFlowModel getDataProcessFlow(String dataProcessFlowName, List<DataProcessNodeEdge> edgeList, AbstractDataProcessNode source, AbstractDataProcessNode target) {
        List<List<AbstractDataProcessNode>> list = DataProcessFlowService.getAllDataProcessFlows(edgeList, source, target);
        return new DataProcessFlowModel(dataProcessFlowName, list);
    }
}
