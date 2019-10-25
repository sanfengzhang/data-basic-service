package com.han.datamgr.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * @author: Hanl
 * @date :2019/10/25
 * @desc:
 */

@Entity
@DiscriminatorValue("cmd_param_config")
@Data
@EqualsAndHashCode(exclude = {"commandEntity"}, callSuper = true)
public class CommandParamEntity extends FiledEntity {

    @ManyToOne
    @JoinColumn(name = "cmd_id")
    private CommandEntity commandEntity;

}
