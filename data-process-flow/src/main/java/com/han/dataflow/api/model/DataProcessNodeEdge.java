package com.han.dataflow.api.model;

import lombok.Data;

/**
 * @author: Hanl
 * @date :2019/8/9
 * @desc:
 */
@Data
public class DataProcessNodeEdge {

    private AbstractDataProcessNode srcNode;

    private AbstractDataProcessNode dstNode;

    public DataProcessNodeEdge(AbstractDataProcessNode srcNode, AbstractDataProcessNode dstNode) {
        this.srcNode = srcNode;
        this.dstNode = dstNode;
    }
}
