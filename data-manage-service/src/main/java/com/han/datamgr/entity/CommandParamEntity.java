package com.han.datamgr.entity;

import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * @author: Hanl
 * @date :2019/10/12
 * @desc:
 */
@Entity
@DiscriminatorValue("cmd_init_param")
@Data
public class CommandParamEntity extends FiledEntity {

    @ManyToOne(cascade = {CascadeType.ALL})
    private CommandInstanceEntity commandInstanceEntity = null;


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
