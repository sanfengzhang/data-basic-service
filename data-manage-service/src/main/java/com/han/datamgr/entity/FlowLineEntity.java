package com.han.datamgr.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author: Hanl
 * @date :2019/10/19
 * @desc:
 */

@Data
@ToString(exclude = {"start","end","flowEntity"})
@EqualsAndHashCode(exclude = {"start","end","flowEntity"})
@Entity
@Table(name = "flow_cmd_line")
public class FlowLineEntity implements Serializable {

    @Id
    @GenericGenerator(name = "jpa-uuid", strategy = "uuid")
    @GeneratedValue(generator = "jpa-uuid")
    private String id;

    @ManyToOne
    @JoinColumn(name = "start_cmd_id")
    @JsonIgnoreProperties(value = {"flowLineSet"})
    private CommandInstanceEntity start;

    @ManyToOne
    @JoinColumn(name = "end_cmd_id")
    @JsonIgnoreProperties(value = {"flowLineSet"})
    private CommandInstanceEntity end;

    @Column(name = "create_time")
    @CreationTimestamp
    private Date createTime;//任务创建时间

    @ManyToOne
    @JoinColumn(name = "flow_id")
    @JsonIgnoreProperties(value = {"flowLineSet","jobFlowRelSet"})
    private DataProcessFlowEntity flowEntity;

    @Column(name = "update_time")
    @LastModifiedDate
    private Date updateTime;//任务更新时间
}
