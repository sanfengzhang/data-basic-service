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
public class CommandEntity implements Serializable {

    private String id;

    private String commandName;

    private String commandType;

    private String commandParams;//命令本身的构建需要的参数，Map<String,Object>的JSON的字符串

    private String commandInputParams;//命令入参，List<FiledTypeEntity>的JSON的字符串

    private String commandOutputParams;//命令出参，List<FiledTypeEntity>的JSON的字符串

    private Date createTime;//数据处理流程创建时间

    private Date updateTime;//数据流程更新时间


}
