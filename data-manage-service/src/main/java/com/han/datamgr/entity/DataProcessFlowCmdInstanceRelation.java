package com.han.datamgr.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author: Hanl
 * @date :2019/9/30
 * @desc:
 */
@Data
@Entity
@Table(name = "data_process_flow_command_instance_rel")
public class DataProcessFlowCmdInstanceRelation implements Serializable {

    @Id
    @GenericGenerator(name = "jpa-uuid", strategy = "uuid")
    @GeneratedValue(generator = "jpa-uuid")
    private String id;

    @ManyToOne(targetEntity = DataProcessFlowEntity.class)
    @JoinColumn(name = "data_process_flow_id")
    private DataProcessFlowEntity dataProcessFlowEntity;

    @ManyToOne(targetEntity = CommandInstanceEntity.class)
    @JoinColumn(name = "cmd_instance_id")
    private CommandInstanceEntity commandInstanceEntity;

    @Column(name = "cmd_order")//命令实例在数据处理流程中的顺序
    private int order;

    @Override
    public String toString() {
        return "DataProcessFlowCmdInstanceRelation{" +
                "id='" + id + '\'' +
                ", dataProcessFlowEntity=" + (dataProcessFlowEntity == null ? "" : dataProcessFlowEntity.getId()) +
                ", commandInstanceEntity=" + (commandInstanceEntity == null ? "" : commandInstanceEntity.getId()) +
                ", order=" + order +
                '}';
    }
}
