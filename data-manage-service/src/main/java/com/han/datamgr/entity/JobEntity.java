package com.han.datamgr.entity;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * @author: Hanl
 * @date :2019/9/29
 * @desc:
 */
@Data
@ToString
public class JobEntity implements Serializable {

    private String id;

    private String jobName;

    private String dataTypeToDataProcessFlow;//该Job处理的所有数据类型，Map<String,DataProcessFlowVO>

    private String jobConfig;//Job级别的配置参数

    private Date createTime;//任务创建时间

    private Date updateTime;//任务更新时间

}
