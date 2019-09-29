package com.han.datamgr.vo;

import com.han.datamgr.entity.JobEntity;
import lombok.Data;
import lombok.ToString;

import java.util.Date;
import java.util.Map;

/**
 * @author: Hanl
 * @date :2019/9/29
 * @desc:
 */
@Data
@ToString
public class JobVO extends BaseVO<JobEntity> {

    private String id;

    private String jobName;

    //FIXME Job关联了那些数据处理流程，当前设计时静态数据类型关联，需要考虑自定方式选择数据处理流程?
    private Map<String,DataProcessFlowVO> dataTypeToDataProcessFlow;//该Job处理的所有数据类类型，对应的数据处理流程

    private Map<String, Object> jobConfig;//Job级别的配置参数

    private Date createTime;//任务创建时间

    private Date updateTime;//任务更新时间

    @Override
    public JobEntity to() {
        return null;
    }

    @Override
    public BaseVO from(JobEntity jobEntity) {
        return null;
    }
}
