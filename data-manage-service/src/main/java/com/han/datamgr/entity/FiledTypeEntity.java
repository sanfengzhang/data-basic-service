package com.han.datamgr.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author: Hanl
 * @date :2019/8/7
 * @desc:
 */
@Data
@Entity
@Table(name = "filed_type")
public class FiledTypeEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GenericGenerator(name = "jpa-uuid", strategy = "uuid")
    @GeneratedValue(generator = "jpa-uuid")
    private String id;

    @Column(name = "filed_name")
    private String fieldName;

    @Column(name = "filed_type")
    private String fieldType;

    @Column(name = "filed_length")
    private int fieldLength;

    @Column(name = "filed_analyzer")
    private String analyzer;

    @Column(name = "filed_format")
    private String format;
}
