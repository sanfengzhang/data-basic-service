package com.han.datamgr.entity;

import lombok.Data;
import org.hibernate.annotations.Cascade;
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
@Table(name = "command_instance")
public class CommandInstanceEntity implements Serializable {
    @Id
    @GenericGenerator(name = "jpa-uuid", strategy = "uuid")
    private String id;

    @Column(name = "cmd_instance_name")
    private String commandInstanceName;

    @Column(name = "cmd_instance_params")
    private String commandInstanceParams;

    @ManyToOne(cascade = {CascadeType.REMOVE})
    @JoinColumn(name = "cmd_id")
    private CommandEntity command;

    @Column(name = "create_time")
    private Date createTime;
}
