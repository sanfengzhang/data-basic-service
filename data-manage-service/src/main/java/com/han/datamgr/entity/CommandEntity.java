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

    @Column(name = "cmd_clazz",columnDefinition = "当前Command所属的原始java类名称")
    private String commandClazz;

    @Column(name = "cmd_type")
    private String commandType;

    @Column(name = "cmd_provider",columnDefinition = "当前command的提供者,系统默认或者外部业务方提供")
    private String commandProvider;

    @Column(name = "create_time")
    @CreationTimestamp
    private Date createTime;//数据处理流程创建时间

    @Column(name = "update_time")
    private Date updateTime;//数据流程更新时间


}
