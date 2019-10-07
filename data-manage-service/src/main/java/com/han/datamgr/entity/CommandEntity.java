package com.han.datamgr.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author: Hanl
 * @date :2019/9/29
 * @desc:
 */
@Data
@Entity
@Table(name = "command")
public class CommandEntity implements Serializable {

    @Id
    @GenericGenerator(name = "jpa-uuid", strategy = "uuid")
    @GeneratedValue(generator = "jpa-uuid")
    private String id;

    @Column(name = "cmd_name")
    private String commandName;

    @Column(name = "cmd_clazz")
    private String commandClazz;//当前Command所属的原始java类名称

    @Column(name = "cmd_type")
    private String commandType;


    @Column(name = "create_time")
    @CreationTimestamp
    private Date createTime;//数据处理流程创建时间

    @Column(name = "update_time")
    private Date updateTime;//数据流程更新时间


}
