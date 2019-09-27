package com.han.datamgr.vo;

import com.han.datamgr.entity.DataProcessFlowEntity;
import lombok.Data;

import java.util.Date;

/**
 * @author: Hanl
 * @date :2019/9/27
 * @desc:
 */
@Data
public class DataProcessFlowVO extends BaseVO<DataProcessFlowEntity> {

    private String id;

    private String dataProcessFlowName;//数据处理流程名称

    private String sourceDesc;//数据源描述

    private String processCommandDetail;//数据处理流程明细，命令配置集合。

    private String filterCmdCondition;//某些命令过滤条件

    private String dstDesc;//目标输出

    private Date createTime;//数据处理流程创建时间

    private Date updateTime;//数据流程更新时间

    @Override
    public DataProcessFlowEntity to() {
        return null;
    }

    @Override
    public BaseVO from(DataProcessFlowEntity dataProcessFlowEntity) {
        return null;
    }
}
