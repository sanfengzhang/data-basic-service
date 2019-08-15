package com.han.dataflow.api;

import com.han.dataflow.api.model.AbstractDataProcessNode;
import com.han.dataflow.api.model.DataProcessFlowModel;
import com.han.dataflow.api.model.DataProcessNodeEdge;

import java.util.List;

/**
 * @author: Hanl
 * @date :2019/8/7
 * @desc:
 */
public interface DataFlowService {

    /**
     * @param edgeList 所有的边
     * @param source   起始节点
     * @param target   结束节点
     * @return
     */
    public DataProcessFlowModel getDataProcessFlow(String dataProcessFlowName, List<DataProcessNodeEdge> edgeList, AbstractDataProcessNode source, AbstractDataProcessNode target);


}


