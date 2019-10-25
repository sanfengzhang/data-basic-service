package com.han.datamgr.vo;

import com.alibaba.fastjson.JSON;
import com.han.datamgr.entity.CommandInstanceEntity;
import com.han.datamgr.entity.CommandInstanceParamEntity;
import lombok.Data;
import lombok.ToString;

import java.util.*;

/**
 * @author: Hanl
 * @date :2019/9/30
 * @desc:
 */
@Data
@ToString
public class CommandInstanceVO extends BaseVO<CommandInstanceEntity> {

    private String id;

    private String commandInstanceName;

    private List<FiledTypeVO> commandParams = new ArrayList<>();//命令本身的构建需要的参数，可以是command参数，子流程调用

    private List<FiledTypeVO> commandInputParams = new ArrayList<>();//命令入参，List<FiledTypeEntity>的JSON的字符串

    private List<FiledTypeVO> commandOutputParams = new ArrayList<>();//命令出参，List<FiledTypeEntity>的JSON的字符串

    //当前命令是否根据Selector逻辑判断跳过执行，用户可以自定一些自己的选择器
    private String skipCmdSelectorClazz;

    //当前命令是否跳过执行过滤条件,可以支持一些ELExpress的bool表达式.定义当skipCmdCondition=Always Skip
    //的时候就总是跳过当前cmd的执行
    private String skipCmdCondition;

    private CommandVO commandVO;

    private Date createTime;

    private String name;

    private String type;

    private String ico = "el-icon-time";

    @Override
    public CommandInstanceEntity to() {
        CommandInstanceEntity entity = new CommandInstanceEntity();
        entity.setId(this.getId());
        entity.setCommand(commandVO.to());
        entity.setCommandInstanceName(this.commandInstanceName);

        Set<CommandInstanceParamEntity> commandInstanceParamEntityList = new HashSet<>();
        commandParams.forEach(commandParam -> {
            commandInstanceParamEntityList.add((CommandInstanceParamEntity) commandParam.to());
        });
        entity.setCmdInstanceParams(commandInstanceParamEntityList);
        entity.setCommandInputParams(JSON.toJSONString(commandInputParams));
        entity.setCommandOutputParams(JSON.toJSONString(commandOutputParams));
        entity.setCreateTime(this.createTime);
        return entity;
    }

    @Override
    public void from(CommandInstanceEntity entity) {
        this.setId(entity.getId());
        List<FiledTypeVO> cmdInstanceParams = new ArrayList<>();
        entity.getCmdInstanceParams().forEach(commandParamEntity -> {
            FiledTypeVO filedTypeVO = new FiledTypeVO();
            filedTypeVO.from(commandParamEntity);
            cmdInstanceParams.add(filedTypeVO);
        });
        this.setCommandParams(cmdInstanceParams);
        this.setCommandInputParams(JSON.parseArray(entity.getCommandInputParams(), FiledTypeVO.class));
        this.setCommandOutputParams(JSON.parseArray(entity.getCommandOutputParams(), FiledTypeVO.class));
        this.setSkipCmdSelectorClazz(entity.getSkipCmdSelectorClazz());
        this.setSkipCmdCondition(entity.getSkipCmdCondition());
        this.setCreateTime(entity.getCreateTime());
        CommandVO commandVO = new CommandVO();
        commandVO.from(entity.getCommand());
        this.setCommandVO(commandVO);
        this.setName(entity.getCommandInstanceName());
        this.setType(entity.getCommand().getCommandType());
    }
}
