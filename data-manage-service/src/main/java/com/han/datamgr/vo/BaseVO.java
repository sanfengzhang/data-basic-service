package com.han.datamgr.vo;

import java.util.List;

/**
 * @author: Hanl
 * @date :2019/9/27
 * @desc:
 */
public abstract class BaseVO<T> implements java.io.Serializable {

    private List<T> entities;

    public abstract T to();

    public abstract void from(T t);

}
