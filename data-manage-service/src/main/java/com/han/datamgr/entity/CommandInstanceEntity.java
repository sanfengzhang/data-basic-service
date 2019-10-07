package com.han.datamgr.entity;

import lombok.Data;
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
    private String commandInstanceName;//cmd实例名称,比如soc的EL算子

    @Column(name = "cmd_instance_params")
    private String commandInstanceParams;//ETL算子的初始化参数

    @Column(name = "cmd_input")
    private String commandInputParams;//cmd的的输入参数，Record中的参数

    @Column(name = "cmd_output")
    private String commandOutputParams;//cmd的输出参数

    @Column(name = "skip_cmd_selector")
    private String skipCmdSelectorClazz;//是否执行当前算子的操作
    ;

    @Column(name = "skip_cmd_condition")
    private String skipCmdCondition;//逻辑表达式

    @OneToMany(targetEntity = DataProcessFlowCmdInstanceRelation.class, mappedBy = "commandInstanceEntity",
            cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DataProcessFlowCmdInstanceRelation> dataFlowCmdInstanceList = new ArrayList<>();//关联哪些数据流程

    @ManyToOne(cascade = {CascadeType.REMOVE})
    @JoinColumn(name = "cmd_id")
    private CommandEntity command;//属于哪个Command的实例对象

    @Column(name = "version")
    private int version;//同一个业务下面的同一个算子，可能在不同的时候使用版本不一样

    @Column(name = "create_time")
    @CreationTimestamp
    private Date createTime;
}
