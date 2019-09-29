package com.han.datamgr.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author: Hanl
 * @date :2019/9/29
 * @desc:
 */
@Data
public class EventEntity implements Serializable {

    private String id;

    private String methodName;

    private String eventDetail;

    private int eventType;

    private String result;

    private int tookTime;//事件耗时

    private Date eventStartTime;//事件开始时间

    private Date createTime;


}
