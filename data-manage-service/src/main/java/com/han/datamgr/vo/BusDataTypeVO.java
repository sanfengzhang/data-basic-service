package com.han.datamgr.vo;

import com.han.datamgr.entity.BusDataTypeEntity;
import com.han.datamgr.support.AlreadyInDB;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;


import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

/**
 * @author: Hanl
 * @date :2019/9/27
 * @desc:
 */
@Data
@ToString
public class BusDataTypeVO extends BaseVO<BusDataTypeEntity> {

    private String id;

    @AlreadyInDB(table = "data_type", where = "data_type_name")
    @NotBlank(message = "数据类型名称不能为空")
    @Length(max = 255)
    private String dataTypeName;

    private List<FiledTypeVO> dataSchema;//

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
    public void from(BusDataTypeEntity busDataTypeEntity) {

    }
}
