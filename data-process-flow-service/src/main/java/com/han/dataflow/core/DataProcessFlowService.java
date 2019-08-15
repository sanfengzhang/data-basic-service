package com.han.dataflow.core;

import com.han.dataflow.api.model.AbstractDataProcessNode;
import com.han.dataflow.api.model.DataProcessNodeEdge;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author: Hanl
 * @date :2019/8/7
 * @desc: 数据处理流程定义的是从数据源到数据持久化的整个过程，这个过程需要对数据进行哪些计算转换
 * 等操作的。也是将原始数据通过一系列的定义能让它输出未目标数据的过程。
 * 这个流程会将基础数据服务和流式计算任务服务组合起来，数据处理流程会将ETL、数据处理规则，
 * 富化等操作关联起来，这些不能放在数据类型上面、因为需要将数据描述、管理服务和流程服务解耦
 * ，一个数据类型生成可能有多个数据处理流程产生数据
 */
@lombok.Data
public class DataProcessFlowService implements Serializable {

    private static final long serialVersionUID = 6521338738790851185L;


    public static List<List<AbstractDataProcessNode>> getAllDataProcessFlows(List<DataProcessNodeEdge> edgeList, AbstractDataProcessNode source, AbstractDataProcessNode target) {

        List<AbstractDataProcessNode> path = new ArrayList<>();
        List<List<AbstractDataProcessNode>> allPaths = new LinkedList<>();
        findPath(edgeList, source, target, path, allPaths);
        return allPaths;
    }

    /**
     * @param edgeList 所有的有向边
     * @param source   起始节点
     * @param target   目的节点
     * @param path     起始节点到目的节点的路径
     * @param allPaths 所有的路径集合
     */
    public static void findPath(List<DataProcessNodeEdge> edgeList, AbstractDataProcessNode source, AbstractDataProcessNode target, List<AbstractDataProcessNode> path, List<List<AbstractDataProcessNode>> allPaths) {
        System.out.println("path:" + path);
        if (path.indexOf(source) > -1) {
            return;
        }
        path = cleanPath(path);
        for (int i = 0; i < edgeList.size(); i++) {
            DataProcessNodeEdge edge = edgeList.get(i);
            if (edge.getSrcNode().equals(source)) {
                //如果相等则找到路径
                if (edge.getDstNode().equals(target)) {
                    path.add(edge.getSrcNode());
                    path.add(edge.getDstNode());
                    List<AbstractDataProcessNode> tmpList = new ArrayList<>();
                    tmpList = cleanPath(path);
                    System.out.println("find path origin:" + path.toString());
                    allPaths.add(tmpList);
                    System.out.println("find path clean:" + tmpList);
                    path.clear();
                    return;
                }
                path.add(edge.getSrcNode());
                findPath(edgeList, edge.getDstNode(), target, path, allPaths);
            }
        }
    }

    public static List<AbstractDataProcessNode> cleanPath(List<AbstractDataProcessNode> path) {
        List<AbstractDataProcessNode> tmp = new ArrayList<>();
        for (AbstractDataProcessNode node : path) {
            if (tmp.indexOf(node) < 0) {
                tmp.add(node);
            }
        }
        return tmp;
    }


}
