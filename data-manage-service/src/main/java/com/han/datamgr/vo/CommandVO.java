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
