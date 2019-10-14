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
    @GeneratedValue(generator = "jpa-uuid")
    private String id;

    @Column(name = "cmd_instance_name", columnDefinition = "cmd实例名称,比如soc的EL算子")
    private String commandInstanceName;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "commandInstanceEntity")
    private List<CommandParamEntity> cmdInstanceParams = new ArrayList<>();

    @Column(name = "cmd_input", columnDefinition = "cmd的的输入参数，Record中的参数")
    private String commandInputParams;

    @Column(name = "cmd_output", columnDefinition = "cmd的输出参数")
    private String commandOutputParams;

    @Column(name = "skip_cmd_selector", columnDefinition = "是否跳过当前算子的操作的选择器")
    private String skipCmdSelectorClazz;
    ;

    @Column(name = "skip_cmd_condition", columnDefinition = "选择器的逻辑表达式")
    private String skipCmdCondition;

    @OneToMany(targetEntity = DataProcessFlowCmdInstanceRelation.class, mappedBy = "commandInstanceEntity",
            cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DataProcessFlowCmdInstanceRelation> dataFlowCmdInstanceList = new ArrayList<>();//关联哪些数据流程

    @ManyToOne(cascade = {CascadeType.REMOVE})
    @JoinColumn(name = "cmd_id", columnDefinition = "属于哪个Command的实例对象")
    private CommandEntity command;

    @Column(name = "version", columnDefinition = "同一个业务下面的同一个算子，可能在不同的时候使用版本不一样")
    private int version;

    @Column(name = "create_time")
    @CreationTimestamp
    private Date createTime;
}
