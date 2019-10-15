package com.han.datamgr.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author: Hanl
 * @date :2019/9/27
 * @desc:
 */
@Data
@Entity
@Table(name = "data_process_flow")
public class DataProcessFlowEntity implements java.io.Serializable {

    @Id
    @GenericGenerator(name = "jpa-uuid", strategy = "uuid")
    @GeneratedValue(generator = "jpa-uuid")
    private String id;

    @Column(name = "data_process_flow_name", columnDefinition = "数据处理流程名称,可以重复")
    private String dataProcessFlowName;

    @Column(name = "load_external_lib_path", columnDefinition = "需要加载外部实现的命令插件,创建数据流程的时候会校验当前command是否都存在！")
    private String loadExternalLibsPath;//

    @Column(name = "filter_cmd_condition", columnDefinition = "某些命令过滤条件")
    private String filterCmdCondition;

    @Column(name = "create_time")
    @CreationTimestamp
    private Date createTime;

    @Column(name = "version")
    private int version;

    @OneToMany(targetEntity = JobDataProcessFlowRelationEntity.class, mappedBy = "dataProcessFlowEntity",
            cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JobDataProcessFlowRelationEntity> relationEntities = new ArrayList<>();


    @OneToMany(targetEntity = DataProcessFlowCmdInstanceRelation.class, mappedBy = "dataProcessFlowEntity",
            cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.EAGER)
    private List<DataProcessFlowCmdInstanceRelation> cmdInstanceEntityList = new ArrayList<>();


}
