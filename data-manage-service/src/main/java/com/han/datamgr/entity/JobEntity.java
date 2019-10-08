package com.han.datamgr.entity;

import com.google.j2objc.annotations.ObjectiveCName;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

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
@Entity
@Table(name = "job")
public class JobEntity implements Serializable {

    @Id
    @GenericGenerator(name = "jpa-uuid", strategy = "uuid")
    @GeneratedValue(generator = "jpa-uuid")
    private String id;

    @Column(name = "job_name")
    private String jobName;

    @Column(name = "job_config_params", columnDefinition = "Job级别的配置参数")
    private String jobConfigParams;

    @OneToMany(targetEntity = JobDataProcessFlowRelationEntity.class, mappedBy = "jobEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JobDataProcessFlowRelationEntity> relationEntities = new ArrayList<>();

    @Column(name = "create_time")
    @CreationTimestamp
    private Date createTime;//任务创建时间

    @Column(name = "update_time")
    @LastModifiedDate
    private Date updateTime;//任务更新时间

}
