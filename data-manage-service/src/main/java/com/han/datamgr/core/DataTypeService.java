package com.han.datamgr.core;

import com.han.datamgr.exception.DataTypeException;
import com.han.datamgr.entity.BusDataTypeEntity;

/**
 * @author: Hanl
 * @date :2019/8/7
 * @desc:
 * 数据类型管理基本服务
 */
public interface DataTypeService {

    /**
     * 创建数据类型
     * @param dataType
     * @throws DataTypeException
     */
    public void createDataType(BusDataTypeEntity dataType) throws DataTypeException;


    /**
     * 更新数据类型
     * @param dataType
     * @throws DataTypeException
     */
    public void updateDataType(BusDataTypeEntity dataType) throws DataTypeException;


    /**
     * 删除数据类型
     * @param dataType
     * @throws DataTypeException
     */
    public void deleteDataType(BusDataTypeEntity dataType)throws DataTypeException;

}
