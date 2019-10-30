package com.han.datamgr.vo;

import com.alibaba.fastjson.JSON;
import com.han.datamgr.entity.CommandInstanceEntity;
import com.han.datamgr.entity.CommandInstanceParamEntity;
import com.han.datamgr.entity.CommandParamEntity;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.*;

/**
 * @author: Hanl
 * @date :2019/9/30
 * @desc:
 */
@Data
@ToString
public class CommandInstanceVO implements Serializable {

    private String commandInstanceName;

    private List<CommandParamEntity> cmdParams=new ArrayList<>();

    private String command;

    private List<String> subFlows=new ArrayList<>();

    private String selectSubFlowClazz;

}
