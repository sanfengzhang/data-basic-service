package com.han.datamgr.vo;

import com.han.datamgr.entity.FiledTypeEntity;
import lombok.Data;
import lombok.ToString;

/**
 * @author: Hanl
 * @date :2019/9/29
 * @desc:
 */
@Data
@ToString
public class FiledTypeVO extends BaseVO<FiledTypeEntity>{

    private String fieldName;

    private String fieldType;

    private int fieldLength;

    private String analyzer;

    private String format;

    @Override
    public FiledTypeEntity to() {
        return null;
    }

    @Override
    public BaseVO from(FiledTypeEntity filedTypeEntity) {
        return null;
    }
}
