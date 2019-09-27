package com.han.datamgr.vo;

import com.han.datamgr.entity.BusDataTypeEntity;
import com.han.datamgr.support.AlreadyInDB;
import lombok.Data;
import org.hibernate.validator.constraints.Length;


import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * @author: Hanl
 * @date :2019/9/27
 * @desc:
 */
@Data
public class BusDataTypeVO extends BaseVO<BusDataTypeEntity> {

    private String id;

    @NotBlank(message = "数据类型名称不能为空")
    @Length(max = 255)
    @AlreadyInDB(sql = "")
    private String dataTypeName;

    private String dataSchema;

    private String dataTypeLineage;

    private String dataStorageDesc;

    private Date createTime;

    private Date updateTime;

    private String desc;

    @Override
    public BusDataTypeEntity to() {

        return null;
    }

    @Override
    public BaseVO from(BusDataTypeEntity busDataTypeEntity) {

        return null;
    }
}
