package com.han.datamgr.entity;

import lombok.Data;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    @Column(name = "cmd_input")
    private String commandInputParams;

    @Column(name = "cmd_output")
    private String commandOutputParams;

    @Column(name = "skip_cmd_selector")
    private String skipCmdSelectorClazz;
    ;

    @Column(name = "skip_cmd_condition")
    private String skipCmdCondition;

    @OneToMany(targetEntity = DataProcessFlowCmdInstanceRelation.class, mappedBy = "commandInstanceEntity",
            cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DataProcessFlowCmdInstanceRelation> dataFlowCmdInstanceList = new ArrayList<>();

    @ManyToOne(cascade = {CascadeType.REMOVE})
    @JoinColumn(name = "cmd_id")
    private CommandEntity command;

    @Column(name = "version")
    private int version;

    @Column(name = "create_time")
    @CreationTimestamp
    private Date createTime;
}
