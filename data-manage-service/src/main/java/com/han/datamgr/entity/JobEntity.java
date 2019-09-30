package com.han.datamgr.entity;

import com.google.j2objc.annotations.ObjectiveCName;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author: Hanl
 * @date :2019/9/29
 * @desc:
 */
@Data
@ToString
@Entity
@Table(name = "job")
public class JobEntity implements Serializable {

    @Id
    @GenericGenerator(name = "jpa-uuid", strategy = "uuid")
    @GeneratedValue(generator = "jpa-uuid")
    private String id;

    @Column(name = "job_name")
    private String jobName;

    @Column(name = "job_config_params")
    private String jobConfigParams;//Job级别的配置参数

//    @ManyToMany
//    @JoinTable(name = "job_data_process_flow_relation", joinColumns = @JoinColumn(name = "job_id",referencedColumnName="id"),
//            inverseJoinColumns = @JoinColumn(name = "data_process_flow_id",referencedColumnName="id"))
//    private List<DataProcessFlowEntity> dataProcessFlowEntityList=new ArrayList<>();

    @OneToMany( targetEntity = JobDataProcessFlowRelationEntity.class,mappedBy = "jobEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JobDataProcessFlowRelationEntity> relationEntities = new ArrayList<>();

    @Column(name = "create_time")
    private Date createTime;//任务创建时间

    @Column(name = "update_time")
    private Date updateTime;//任务更新时间

}
