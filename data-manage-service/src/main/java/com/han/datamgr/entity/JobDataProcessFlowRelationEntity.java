package com.han.datamgr.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author: Hanl
 * @date :2019/9/30
 * @desc:
 */
@Data
@Entity
@Table(name = "job_data_process_flow_relation")
public class JobDataProcessFlowRelationEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GenericGenerator(name = "jpa-uuid", strategy = "uuid")
    @GeneratedValue(generator = "jpa-uuid")
    private String id;


    @ManyToOne(targetEntity = JobEntity.class)
    @JoinColumn(name="job_id")
    private JobEntity jobEntity;

    @ManyToOne(targetEntity = DataProcessFlowEntity.class)
    @JoinColumn(name="data_process_flow_id")
    private DataProcessFlowEntity dataProcessFlowEntity;

    @Column(name = "create_time")
    private Date createTime;
}
