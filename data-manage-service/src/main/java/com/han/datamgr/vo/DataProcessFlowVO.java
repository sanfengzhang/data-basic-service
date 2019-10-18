package com.han.datamgr.vo;

import com.han.datamgr.entity.DataProcessFlowCmdInstanceRelation;
import com.han.datamgr.entity.DataProcessFlowEntity;
import lombok.Data;
import lombok.ToString;
import javax.validation.constraints.NotBlank;

import java.util.*;

/**
 * @author: Hanl
 * @date :2019/9/27
 * @desc:
 */
@Data
@ToString
public class DataProcessFlowVO extends BaseVO<DataProcessFlowEntity> {

    private String id;

    @NotBlank(message = "流程名称不能为空")
    private String dataProcessFlowName;//数据处理流程名称

    private List<CommandInstanceVO> processCommandDetail=new ArrayList<>();//数据处理流程明细，命令配置集合。

    private String loadExternalLibsPath;//需要加载外部实现的命令插件,创建数据流程的时候会校验当前command是否都存在！

    private String filterCmdCondition;

    private Date createTime;//数据处理流程创建时间

    private int version;

    @Override
    public DataProcessFlowEntity to() {
        DataProcessFlowEntity flowEntity = new DataProcessFlowEntity();
        if (null != id) {
            flowEntity.setId(id);
        }
        flowEntity.setDataProcessFlowName(dataProcessFlowName);
        flowEntity.setLoadExternalLibsPath(loadExternalLibsPath);
        if (null != createTime) {
            flowEntity.setCreateTime(createTime);
        }
        flowEntity.setVersion(version);
        Set<DataProcessFlowCmdInstanceRelation> relations=new HashSet<>();
        for(CommandInstanceVO commandInstanceVO:processCommandDetail){
            DataProcessFlowCmdInstanceRelation relation=new DataProcessFlowCmdInstanceRelation();
            relation.setCommandInstanceEntity(commandInstanceVO.to());
            relation.setDataProcessFlowEntity(flowEntity);
            //FIXME
           // relation.setOrder();
            relations.add(relation);
        }
        flowEntity.setCmdInstanceEntityList(relations);
        return flowEntity;
    }

    @Override
    public void from(DataProcessFlowEntity dataProcessFlowEntity) {

    }
}
