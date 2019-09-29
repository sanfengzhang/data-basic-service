package com.han.datamgr.vo;

import com.han.datamgr.entity.CommandEntity;
import lombok.Data;
import lombok.ToString;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author: Hanl
 * @date :2019/9/29
 * @desc:
 */
@Data
@ToString
public class CommandVO extends BaseVO<CommandEntity> {

    private String id;

    private String commandName;

    private String commandClazz;//当前Command所属的原始java类名称

    private String commandType;//按Command功能分类型，解析类型、富化类型

    private Map<String, Object> commandParams;//命令本身的构建需要的参数，可以是command参数，子流程调用

    private List<FiledTypeVO> commandInputParams;//命令入参，List<FiledTypeEntity>的JSON的字符串

    private List<FiledTypeVO> commandOutputParams;//命令出参，List<FiledTypeEntity>的JSON的字符串

    //当前命令是否根据Selector逻辑判断跳过执行，用户可以自定一些自己的选择器
    private String skipCmdSelectorClazz;

    //当前命令是否跳过执行过滤条件,可以支持一些ELExpress的bool表达式.定义当skipCmdCondition=Always Skip
    //的时候就总是跳过当前cmd的执行
    private String skipCmdCondition;

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
