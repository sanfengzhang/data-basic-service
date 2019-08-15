package com.han.dataflow.api.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author: Hanl
 * @date :2019/8/7
 * @desc: 抽象成每一个数据处理节点、这对应的是一个计算任务，通过对这个节点的定义
 * 将DataProcessNode--transform--->Stream Operator，可以将这些算子抽象以下几种：
 * 1.涉及对数据的更新、状态变化类型的算子
 * 2.查询、统计类的算子。（这个可以统一根据Flink SQL来解决问题）
 * 3.复杂事件规则算子
 */
@Data
public class AbstractDataProcessNode implements Serializable {

    private String dataProcessNodeName;

    private String desc;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractDataProcessNode that = (AbstractDataProcessNode) o;
        return Objects.equals(dataProcessNodeName, that.dataProcessNodeName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dataProcessNodeName);
    }
}
