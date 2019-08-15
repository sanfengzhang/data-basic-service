package com.han.dataflow.api.model;

/**
 * @author: Hanl
 * @date :2019/8/7
 * @desc:
 */
public enum DataProcessFlowSource {

    ES("ES", 1), MYSQL("mysql", 2), HADOOP("hadoop", 3), KAFKA("KAFKA", 4), SYSLOG("SYSLOG", 5), SOCKET("SOCKET", 6), FTP("FTP", 7);

    private int index;

    private String name;

    private DataProcessFlowSource(String name, int index) {
        this.name = name;
        this.index = index;
    }
}
