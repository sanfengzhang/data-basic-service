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
    private Set<CommandInstanceFlowRelation> cmdInstanceFowRelSet = new HashSet<>();


    @Column(name = "cmd_input")
    private String commandInputParams;//cmd的的输入参数，Record中的参数

    @Column(name = "cmd_output")//cmd的输出参数
    private String commandOutputParams;

    @Column(name = "skip_cmd_selector")//是否跳过当前算子的操作的选择器
    private String skipCmdSelectorClazz;

    @Column(name = "skip_cmd_condition")//选择器的逻辑表达式
    private String skipCmdCondition;

    @ManyToOne
    @JoinColumn(name = "cmd_id")
    @JsonIgnoreProperties(value = {"commandInstanceEntityList"})
    private CommandEntity command;//属于哪个Command的实例对象


    @Column(name = "version")//同一个业务下面的同一个算子，可能在不同的时候使用版本不一样
    private int version;

    @Column(name = "left_px")
    private String left;

    @Column(name = "top")
    private String top;

    @Column(name = "ico")
    private String ico;

    @Column(name = "show_cmd", columnDefinition = "TINYINT(1)")
    private boolean show;

    @Column(name = "create_time")
    @CreationTimestamp
    private Date createTime;

}
