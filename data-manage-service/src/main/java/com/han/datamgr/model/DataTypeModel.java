package com.han.datamgr.model;

import lombok.Data;

import java.util.List;

/**
 * @author: Hanl
 * @date :2019/8/7
 * @desc:
 */
@Data
public class DataTypeModel {

    private String dataTypeName;//数据类型名称、和业务关联

    private List<FiledTypeModel> dataSchema;//描述数据结构的schema,JSON

    private String dataTypeLineage;//描述该类型数据的生成处理过程，可能有多种方式

    private StorageEnum storageEnum;

    private String desc;
}
