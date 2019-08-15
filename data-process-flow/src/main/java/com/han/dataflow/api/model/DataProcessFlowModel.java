package com.han.dataflow.api.model;

import com.google.common.base.Preconditions;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Hanl
 * @date :2019/8/15
 * @desc:
 */
@Data
public class DataProcessFlowModel {

    private String dataProcessFlowName;

    private List<List<AbstractDataProcessNode>> dataProcessFlow;

    public static final String NEXT_STEP = "-->";

    public DataProcessFlowModel(String dataProcessFlowName, List<List<AbstractDataProcessNode>> dataProcessFlow) {
        Preconditions.checkNotNull(dataProcessFlow, "dataProcessFlow must not be null.");
        this.dataProcessFlowName = dataProcessFlowName;
        this.dataProcessFlow = dataProcessFlow;
    }



    /**
     * 获取这个数据处理流程的的描述
     *
     * @return
     */
    public List<List<String>> getDataFlowDescription() {
        List<List<String>> result = new ArrayList<>();

        this.dataProcessFlow.forEach(list -> {
            List<String> list1 = new ArrayList<>();
            list.forEach(abstractDataProcessNode -> {
                list1.add(abstractDataProcessNode.getDesc());
            });
            result.add(list1);
        });

        return result;
    }


    public int countDataProcessFlows() {

        return dataProcessFlow.size();
    }
}
