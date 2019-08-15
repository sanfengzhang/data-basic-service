package com.han.datamgr.core;

import com.han.datamgr.exception.DataTypeException;
import com.han.datamgr.model.DataTypeModel;

/**
 * @author: Hanl
 * @date :2019/8/7
 * @desc:
 */
public interface DataTypeService {

    public void createDataType(DataTypeModel dataTypeModel) throws DataTypeException;


    public void deleteDataType(DataTypeModel dataTypeModel)throws DataTypeException;

}
