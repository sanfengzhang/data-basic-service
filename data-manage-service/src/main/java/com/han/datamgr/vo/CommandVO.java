package com.han.datamgr.vo;

import com.han.datamgr.entity.CommandEntity;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author: Hanl
 * @date :2019/9/29
 * @desc:
 */
@Data
public class CommandVO extends BaseVO<CommandEntity> {

    private String id;

    private String commandName;

    private String getCommandType;

    private List<FiledTypeVO> commandParams;//命令本身的构建需要的参数，List<FiledTypeEntity>的JSON的字符串

    private List<FiledTypeVO> commandInputParams;//命令入参，List<FiledTypeEntity>的JSON的字符串

    private List<FiledTypeVO> commandOutputParams;//命令出参，List<FiledTypeEntity>的JSON的字符串

    private Date createTime;//数据处理流程创建时间

    private Date updateTime;//数据流程更新时间

    @Override
    public CommandEntity to() {
        return null;
    }

    @Override
    public BaseVO from(CommandEntity commandEntity) {
        return null;
    }
}
