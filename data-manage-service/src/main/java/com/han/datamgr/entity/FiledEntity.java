package com.han.datamgr.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: Hanl
 * @date :2019/8/7
 * @desc:
 */
@Data
@Entity
@Table(name = "filed_type")
@DiscriminatorColumn(name = "DISCRIMINATOR", discriminatorType = DiscriminatorType.STRING, length = 30)
@DiscriminatorValue("filed_entity")
public class FiledEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GenericGenerator(name = "jpa-uuid", strategy = "uuid")
    @GeneratedValue(generator = "jpa-uuid")
    private String id;

    @Basic
    @Column(name = "filed_name")
    private String fieldName;

    @Basic
    @Column(name = "filed_type")
    private String fieldType;

    @Column(name = "filed_value",columnDefinition = "varchar(255) COMMENT '字段赋值'")
    private String filedValue;

    @Column(name = "filed_format",columnDefinition = "varchar(32) COMMENT '数据格式化,yyyy-MM-dd'")
    private String format;

}
