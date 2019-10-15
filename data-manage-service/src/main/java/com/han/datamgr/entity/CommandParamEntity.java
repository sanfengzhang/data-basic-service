package com.han.datamgr.entity;

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
public class CommandParamEntity extends FiledEntity {

    private String cmdDisplayName;

    @ManyToOne
    private CommandInstanceEntity commandInstanceEntity = null;

    //private int paramIndex;//参数在命令初始化时候的数组位置，可以用反射的方法直接构建


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
