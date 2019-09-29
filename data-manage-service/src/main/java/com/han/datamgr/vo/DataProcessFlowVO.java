package com.han.datamgr.vo;

import com.han.datamgr.entity.DataProcessFlowEntity;
import lombok.Data;
import lombok.ToString;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author: Hanl
 * @date :2019/9/27
 * @desc:
 */
@Data
@ToString
public class DataProcessFlowVO extends BaseVO<DataProcessFlowEntity> {

    private String id;

    private String dataProcessFlowName;//数据处理流程名称

    private String sourceDesc;//数据源描述

    private List<CommandVO> processCommandDetail;//数据处理流程明细，命令配置集合。

    private String loadExternalLibsPath;//需要加载外部实现的命令插件,创建数据流程的时候会校验当前command是否都存在！

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
