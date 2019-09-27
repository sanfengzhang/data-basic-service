package com.han.datamgr.entity;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * @author: Hanl
 * @date :2019/9/27
 * @desc:
 */
@Data
@ToString
public class DataProcessFlowEntity implements java.io.Serializable {

    private String id;

    private String dataProcessFlowName;//数据处理流程名称

    private String sourceDesc;//数据源描述

    private String processCommandDetail;//数据处理流程明细，命令配置集合。

    private String filterCmdCondition;//某些命令过滤条件

    private String dstDesc;//目标输出

    private Date createTime;//数据处理流程创建时间

    private Date updateTime;//数据流程更新时间


}
