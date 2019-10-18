package com.han.datamgr.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;

/**
 * @author: Hanl
 * @date :2019/9/27
 * @desc:
 */
@Data
@ToString(exclude={"relationEntities","cmdInstanceEntityList"})
@EqualsAndHashCode(exclude={"relationEntities","cmdInstanceEntityList"})
@Entity
@Table(name = "data_process_flow")
public class DataProcessFlowEntity implements java.io.Serializable {

    @Id
    @GenericGenerator(name = "jpa-uuid", strategy = "uuid")
    @GeneratedValue(generator = "jpa-uuid")
    private String id;

    @Column(name = "data_process_flow_name")
    private String dataProcessFlowName;

    @Column(name = "load_external_lib_path")
    private String loadExternalLibsPath;//需要加载外部实现的命令插件,创建数据流程的时候会校验当前command是否都存在！

    @Column(name = "filter_cmd_condition")//, columnDefinition = "某些命令过滤条件"
    private String filterCmdCondition;

    @Column(name = "create_time")
    @CreationTimestamp
    private Date createTime;

    @Column(name = "version")
    private int version;

    @OneToMany(targetEntity = JobDataProcessFlowRelationEntity.class, mappedBy = "dataProcessFlowEntity",
            cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.EAGER)
    private Set<JobDataProcessFlowRelationEntity> relationEntities = new HashSet<>();


    @OneToMany(targetEntity = DataProcessFlowCmdInstanceRelation.class, mappedBy = "dataProcessFlowEntity",
            cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.EAGER)
    @OrderBy("cmdOrder ASC")
    private Set<DataProcessFlowCmdInstanceRelation> cmdInstanceEntityList = new HashSet<>();


}
