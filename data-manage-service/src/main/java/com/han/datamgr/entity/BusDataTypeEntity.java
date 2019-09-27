package com.han.datamgr.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author: Hanl
 * @date :2019/8/7
 * @desc:
 */
@Data
public class BusDataTypeEntity implements java.io.Serializable {

    private String id;

    private String dataTypeName;//数据类型名称、和业务关联

    private String dataSchema;//描述数据结构的schema

    private String dataTypeLineage;//描述该类型数据的生成处理过程，可能有多种方式

    private String dataStorageDesc;//该数据类型持久化策略描述

    private Date createTime;//数据类型创建时间

    private Date updateTime;//数据类型更新时间

    private String desc;
}
