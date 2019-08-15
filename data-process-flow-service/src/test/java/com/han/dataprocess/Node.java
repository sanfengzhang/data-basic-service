package com.han.dataprocess;

import java.util.Objects;
import java.util.Set;

/**
 * @author: Hanl
 * @date :2019/8/9
 * @desc:
 */
public class Node<T> {

    private T data;

    public Node(T data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node<?> node = (Node<?>) o;
        return Objects.equals(data, node.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data);
    }

    @Override
    public String toString() {
        return "Node{" +
                "data=" + data +
                '}';
    }
}
