package com.han.stream.flink;

import com.han.dataflow.api.model.AbstractDataProcessNode;

/**
 * @author: Hanl
 * @date :2019/8/9
 * @desc:
 */
public class FlinkProcessNode extends AbstractDataProcessNode {

         private int parallelism;

         private String operatorName;//默认为操作节点的名称

         private String OP_TYPE;


}
