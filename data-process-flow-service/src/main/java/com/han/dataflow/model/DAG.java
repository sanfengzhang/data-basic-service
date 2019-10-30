package com.han.dataflow.model;

import lombok.Data;

import java.util.*;

/**
 * @author: Hanl
 * @date :2019/8/9
 * @desc:
 */
@Data
public class DAG<T> {

    private List<List<Node<T>>> allPath;

    public DAG(List<Map<T, T>> edgeListMap, T src, T dst) {
        List<Edge<T>> edgeList = new ArrayList<>();
        for (Map<T, T> edgeMap : edgeListMap) {
            Iterator<Map.Entry<T, T>> it = edgeMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<T, T> entry = it.next();
                Edge<T> edge = new Edge<T>(entry.getKey(), entry.getValue());
                edgeList.add(edge);
            }
        }
        findAllPath(edgeList, new Node<T>(src), new Node<T>(dst), new ArrayList<>(), new ArrayList<>());
    }

    public DAG(List<Edge<T>> edgeList, Node<T> srcNode, Node<T> dstNode) {

        findAllPath(edgeList, srcNode, dstNode, new ArrayList<>(), new ArrayList<>());
    }




    public void findAllPath(List<Edge<T>> edgeList, Node<T> srcNode, Node<T> dstNode, List<Node<T>> path, List<List<Node<T>>> allPaths) {
        if (path.indexOf(srcNode) > -1) {
            return;
        }
        path = cleanPath(path);
        for (int i = 0; i < edgeList.size(); i++) {
            Edge edge = edgeList.get(i);
            if (edge.srcNode.equals(srcNode)) {
                //如果相等则找到路径
                if (edge.dstNode.equals(dstNode)) {
                    path.add(edge.srcNode);
                    path.add(edge.dstNode);
                    List<Node<T>> tmpList = new ArrayList<Node<T>>();
                    tmpList = cleanPath(path);
                    allPaths.add(tmpList);
                    path.clear();
                    return;
                }
                path.add(edge.srcNode);
                findAllPath(edgeList, edge.dstNode, dstNode, path, allPaths);
            }
        }
        this.allPath = allPaths;
    }

    public List<Node<T>> cleanPath(List<Node<T>> path) {
        List<Node<T>> tmp = new ArrayList<>();
        for (Node<T> node : path) {
            if (tmp.indexOf(node) < 0) {
                tmp.add(node);
            }
        }
        return tmp;
    }
}



