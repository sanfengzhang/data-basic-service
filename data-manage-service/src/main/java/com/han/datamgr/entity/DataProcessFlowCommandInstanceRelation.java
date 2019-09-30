package com.han.datamgr.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author: Hanl
 * @date :2019/9/30
 * @desc:
 */
@Data
@Entity
@Table(name = "data_process_flow_command_instance_rel")
public class DataProcessFlowCommandInstanceRelation implements Serializable {

    @Id
    @GenericGenerator(name = "jpa-uuid", strategy = "uuid")
    private String id;

    @Column(name = "data_process_flow_id")
    private String dataProcessFlowId;

    @Column(name = "cmd_instance_id")
    private String commandInstanceId;

    @Column(name = "cmd_order")
    private int order;

}
