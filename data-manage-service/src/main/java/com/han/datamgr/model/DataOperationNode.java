package com.han.datamgr.model;

import lombok.Data;

import java.util.Objects;

/**
 * @author: Hanl
 * @date :2019/8/7
 * @desc:
 */
@Data
public class DataOperationNode {

    private String id;

    private String operation;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataOperationNode that = (DataOperationNode) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(operation, that.operation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, operation);
    }
}
