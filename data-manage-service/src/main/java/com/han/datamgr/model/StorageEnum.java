package com.han.datamgr.model;

/**
 * @author: Hanl
 * @date :2019/8/7
 * @desc:
 */
public enum StorageEnum {

    ES("ES", 1), MYSQL("mysql", 2), HADOOP("hadoop", 3),    ;

    private int index;

    private String name;

    private StorageEnum(String name, int index) {
        this.name = name;
        this.index = index;
    }
}
