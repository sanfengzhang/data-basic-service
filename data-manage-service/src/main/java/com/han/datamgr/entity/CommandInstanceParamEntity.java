package com.han.datamgr.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * @author: Hanl
 * @date :2019/10/12
 * @desc:
 */
@Entity
@DiscriminatorValue("cmd_init_param")
@Data
@EqualsAndHashCode(exclude = {"commandInstanceEntity"}, callSuper = true)
public class CommandInstanceParamEntity extends FiledEntity {

    @ManyToOne
    private CommandInstanceEntity commandInstanceEntity;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "cmd_param_id")
    private CommandParamEntity commandParamEntity;


    @Override
    public String toString() {
        String commandInstanceEntityId = "";
        if (null != commandInstanceEntity) {
            commandInstanceEntityId = commandInstanceEntity.getId();
        }
        return "CommandParamEntity{" +
                "cmdDisplayName='" + cmdDisplayName + '\'' +
                ", id='" + id + '\'' +
                ", fieldName='" + fieldName + '\'' +
                ", fieldType='" + fieldType + '\'' +
                ", filedValue='" + filedValue + '\'' +
                ", format='" + format + '\'' +
                ", commandInstanceEntity='" + commandInstanceEntityId + '\'' +
                '}';
    }
}
