package com.han.datamgr.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

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

    @Column(name = "cmd_instance_name")
    @JsonProperty("name")
    private String commandInstanceName;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "commandInstanceEntity", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JsonIgnoreProperties(value = {"commandInstanceEntity"})
    private Set<CommandParamEntity> cmdInstanceParams = new HashSet<>();


    @OneToMany(fetch = FetchType.EAGER, mappedBy = "commandInstanceEntity", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JsonIgnoreProperties(value = {"commandInstanceEntity"})
    private Set<CommandInstanceFlowRelation> cmdInstanceFowRelSet = new HashSet<>();//该command实例节点包含的子流程关系

    @Column(name = "cmd_input")
    private String commandInputParams;//cmd的的输入参数，Record中的参数

    @Column(name = "cmd_output")//cmd的输出参数
    private String commandOutputParams;

    @Column(name = "skip_cmd_selector")//是否跳过当前算子的操作的选择器
    private String skipCmdSelectorClazz;

    @Column(name = "skip_cmd_condition")//选择器的逻辑表达式
    private String skipCmdCondition;

    @Column(name = "select_sub_flow_clazz")
    private String selectSubFlowClazz;//某個节点下有一些子流程，可能存在多个子流程，该怎么选择子流程的运行

    @ManyToOne
    @JoinColumn(name = "cmd_id")
    @JsonIgnoreProperties(value = {"commandInstanceEntityList"})
    private CommandEntity command;//属于哪个Command的实例对象

    @Column(name = "version")//同一个业务下面的同一个算子，可能在不同的时候使用版本不一样
    private int version;

    @Column(name = "create_time")
    @CreationTimestamp
    private Date createTime;

}
