package com.han.datamgr.vo;

import com.han.datamgr.entity.FiledEntity;
import lombok.Data;
import lombok.ToString;

/**
 * @author: Hanl
 * @date :2019/9/29
 * @desc:
 */
@Data
@ToString
public class FiledTypeVO extends BaseVO<FiledEntity>{

    private String fieldName;

    private String fieldType;

    private String filedValue;

    private int fieldLength;

    private String analyzer;

    private String format;

    @Override
    public FiledEntity to() {
        return null;
    }

    @Override
    public void from(FiledEntity filedEntity) {

    }
}
