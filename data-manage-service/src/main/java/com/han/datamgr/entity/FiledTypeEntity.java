package com.han.datamgr.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: Hanl
 * @date :2019/8/7
 * @desc:
 */
@Data
public class FiledTypeEntity implements Serializable {

    private String fieldName;

    private String fieldType;

    private int fieldLength;

    private String analyzer;

    private String format;
}
