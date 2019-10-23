package com.han.datamgr.utils;

import java.util.*;

/**
 * @author: Hanl
 * @date :2019/10/23
 * @desc:
 */

public class FlowUtils {

    /**
     * 从flow的连线集合变换主找到一个流程节点list
     *
     * @param flowLine
     * @param start
     * @param end
     * @return
     */
    public static List<String> findFlow(List<Map<String, String>> flowLine, String start, String end) {
        List<String> result = new ArrayList<>();
        List<Map<String, String>> mainFlowLine = findMainFlowLine(flowLine, start, end);
        TreeSet<String> treeSet = new TreeSet<>();
        for (Map<String, String> map : mainFlowLine) {
            Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> en = it.next();
                treeSet.add(en.getKey());
                if (null != en.getValue()) {
                    treeSet.add(en.getValue());
                }
            }

        }
        String[] arr = new String[treeSet.size()];
        treeSet.toArray(arr);
        result = Arrays.asList(arr);
        return result;
    }

    public static List<Map<String, String>> findMainFlowLine(List<Map<String, String>> flowLine, String start, String end) {
        List<Edge> flowLineEge = new ArrayList<>();
        for (Map<String, String> line : flowLine) {
            Edge<String> edge = new Edge(line.get("from"), line.get("to"));
            flowLineEge.add(edge);
        }
        List<Node> path = new ArrayList<>();
        List<List<Node>> allPaths = new LinkedList<>();
        DAG.findPath(flowLineEge, new Node(start), new Node(end), path, allPaths);
        List<Node> mainFlowNode = new ArrayList<>();
        List<Node> first = allPaths.get(0);
        if (allPaths.size() > 1) {
            //-------找到存在于所有的额path中的节点
            for (Node node : first) {
                boolean flag = true;
                for (int i = 1; i < allPaths.size(); i++) {
                    if (!allPaths.get(i).contains(node)) {
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    mainFlowNode.add(node);
                }
            }
        } else {
            for (Node node : first) {
                mainFlowNode.add(node);
            }
        }
        List<Map<String, String>> result = new ArrayList<>();
        if (mainFlowNode.size() > 1) {
            for (int i = 0; i < mainFlowNode.size() - 1; i++) {
                Map<String, String> line = new HashMap<>();
                line.put(mainFlowNode.get(i).getData().toString(), mainFlowNode.get(i + 1).getData().toString());
                result.add(line);
            }
        } else {
            Map<String, String> line = new HashMap<>();
            line.put(mainFlowNode.get(0).getData().toString(), null);
            result.add(line);
        }
        return result;
    }


    public static Map<String, List<List<String>>> findAllSubFlow(List<Map<String, String>> flowLine, String start, String end) {
        List<Edge> flowLineEge = new ArrayList<>();
        for (Map<String, String> line : flowLine) {
            Edge<String> edge = new Edge(line.get("from"), line.get("to"));
            flowLineEge.add(edge);
        }
        Map<Node, List<List<Node>>> startAndEnd = findAllSubFlowByNode(flowLineEge, new Node(start), new Node(end));
        if (null == startAndEnd || startAndEnd.size() == 0) {
            return null;
        }
        Iterator<Map.Entry<Node, List<List<Node>>>> it = startAndEnd.entrySet().iterator();
        Map<String, List<List<String>>> result = new HashMap<>();
        while (it.hasNext()) {
            Map.Entry<Node, List<List<Node>>> en = it.next();
            List<List<Node>> list = en.getValue();
            List<List<String>> ls = new ArrayList<>();
            list.forEach(data -> {
                List<String> r = new ArrayList<>();
                List<Node> tmp = data.subList(1, data.size());
                tmp.forEach(l -> {
                    r.add(l.getData().toString());
                });
                ls.add(r);
            });
            result.put(en.getKey().getData().toString(), ls);
        }
        return result;
    }

    public static Map<Node, List<List<Node>>> findAllSubFlowByNode(List<Edge> flowLine, Node start, Node end) {
        Map<Node, Node> startAndEnd = findAllSubFlow(flowLine, start, end);
        if (null == startAndEnd || startAndEnd.size() == 0) {
            return null;
        }
        Iterator<Map.Entry<Node, Node>> it = startAndEnd.entrySet().iterator();
        Map<Node, List<List<Node>>> result = new HashMap<>();
        while (it.hasNext()) {
            Map.Entry<Node, Node> en = it.next();
            List<Node> path = new ArrayList<>();
            List<List<Node>> allPaths = new LinkedList<>();
            DAG.findPath(flowLine, en.getKey(), en.getValue(), path, allPaths);
            result.put(en.getKey(), allPaths);
        }

        return result;
    }


    /**
     * 获取流程途中的分支流程
     *
     * @param flowLine
     * @param start
     * @param end
     * @return
     */
    public static Map<Node, Node> findAllSubFlow(List<Edge> flowLine, Node start, Node end) {
        if (flowLine.size() == 0) {
            return null;
        }
        Map<Node, Node> flowMap = new HashMap<>();
        List<Node> path = new ArrayList<>();
        List<List<Node>> allPaths = new LinkedList<>();
        DAG.findPath(flowLine, start, end, path, allPaths);
        if (allPaths.size() <= 1) {//只有一条数据的时候就是没有分支直接返回
            return null;
        }
        //-------找到存在于所有的额path中的节点
        List<Node> mainFlowNode = new ArrayList<>();
        List<Node> first = allPaths.get(0);
        for (Node node : first) {
            boolean flag = true;
            for (int i = 1; i < allPaths.size(); i++) {
                if (!allPaths.get(i).contains(node)) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                mainFlowNode.add(node);
            }
        }

        //----------获取所有的from子流程节点,出边是大于1的
        Map<Node, Integer> startSubFlowNode = new HashMap<>();
        Map<Node, Integer> endSubFlowNode = new HashMap<>();
        flowLine.forEach(line -> {
            Node src = line.srcNode;
            Node dst = line.dstNode;
            int startCount = startSubFlowNode.get(src) == null ? 0 : startSubFlowNode.get(src);
            startCount++;
            int endCount = endSubFlowNode.get(dst) == null ? 0 : endSubFlowNode.get(dst);
            endCount++;
            startSubFlowNode.put(src, startCount);
            endSubFlowNode.put(dst, endCount);
        });
        //--------------------------------

        Iterator<Map.Entry<Node, Integer>> from = startSubFlowNode.entrySet().iterator();
        Iterator<Map.Entry<Node, Integer>> to = endSubFlowNode.entrySet().iterator();
        while (from.hasNext()) {
            if (from.next().getValue() < 2) {
                from.remove();
            }
        }
        while (to.hasNext()) {
            if (to.next().getValue() < 2) {
                to.remove();
            }
        }

        //--------------------
        int size = mainFlowNode.size();
        Set<Node> startNodeSet = startSubFlowNode.keySet();
        Set<Node> endNodeSet = endSubFlowNode.keySet();
        for (Node node : startNodeSet) {
            int index = mainFlowNode.indexOf(node);
            for (int j = index + 1; j < size; j++) {
                if (endNodeSet.contains(mainFlowNode.get(j))) {
                    flowMap.put(node, mainFlowNode.get(j));
                    break;
                }
            }
        }
        return flowMap;
    }

    public static void main(String[] args) {
        List<Edge> edgeList = new ArrayList<>();
        Edge edge = new Edge(1, 2);
        Edge edge2 = new Edge(1, 3);
        Edge edge8 = new Edge(1, 10);
        Edge edge9 = new Edge(10, 11);
        Edge edge10 = new Edge(11, 4);
        Edge edge3 = new Edge(2, 4);
        Edge edge4 = new Edge(3, 4);
        Edge edge7 = new Edge(4, 5);
        edgeList.add(edge);
        edgeList.add(edge2);
        edgeList.add(edge3);
        edgeList.add(edge4);
        edgeList.add(edge7);
        edgeList.add(edge8);
        edgeList.add(edge9);
        edgeList.add(edge10);
        System.out.println(findAllSubFlow(edgeList, edge.srcNode, edge7.dstNode));

        List<Node> path = new ArrayList<>();
        List<List<Node>> allPaths = new LinkedList<>();
        DAG.findPath(edgeList, edge.srcNode, edge3.dstNode, path, allPaths);
        System.out.println(allPaths);

        System.out.println(findAllSubFlowByNode(edgeList, edge.srcNode, edge7.dstNode));
    }
}
