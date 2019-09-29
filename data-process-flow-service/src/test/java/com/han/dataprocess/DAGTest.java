package com.han.dataprocess;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author: Hanl
 * @date :2019/8/30
 * @desc:
 */
public class DAGTest {


    List<Edge> edgeList = new ArrayList<>();


    @Before
    public void setup() {
        Edge edge = new Edge(1, 2);
        Edge edge2 = new Edge(1, 6);
        Edge edge3 = new Edge(2, 4);
        Edge edge4 = new Edge(2, 5);
        Edge edge7 = new Edge(4, 3);
        Edge edge1 = new Edge(1, 4);
        Edge edge8 = new Edge(5, 7);
        Edge edge9 = new Edge(6, 7);
        Edge edge6 = new Edge(3, 7);
        Edge edge5 = new Edge(3, 2);
        edgeList.add(edge);
        edgeList.add(edge1);
        edgeList.add(edge2);
        edgeList.add(edge3);
        edgeList.add(edge4);
        edgeList.add(edge5);
        edgeList.add(edge6);
        edgeList.add(edge7);
        edgeList.add(edge8);
        edgeList.add(edge9);

    }

    @Test
    public void test() {

        List<Node> path = new ArrayList<>();
        List<List<Node>> allPaths = new LinkedList<>();

        DAG.findPath(edgeList, new Node(1), new Node(4), path, allPaths);

        System.out.println(allPaths);
        System.out.println();
    }
}
