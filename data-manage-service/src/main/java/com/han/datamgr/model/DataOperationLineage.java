package com.han.datamgr.model;

import java.util.Objects;
import java.util.Set;

/**
 * @author: Hanl
 * @date :2019/8/7
 * @desc:
 */
public class DataOperationLineage {

    private Set<DataOperationNode> preNode;

    private Set<DataOperationNode> nextNode;

}
