package com.han.datamgr.core;

import com.han.datamgr.entity.BusDataTypeEntity;
import com.han.datamgr.exception.BusException;

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
     * @throws BusException
     */
    public void createDataType(BusDataTypeEntity dataType) throws BusException;


    /**
     * 更新数据类型
     * @param dataType
     * @throws BusException
     */
    public void updateDataType(BusDataTypeEntity dataType) throws BusException;


    /**
     * 删除数据类型
     * @param dataType
     * @throws BusException
     */
    public void removeDataType(BusDataTypeEntity dataType)throws BusException;

}
