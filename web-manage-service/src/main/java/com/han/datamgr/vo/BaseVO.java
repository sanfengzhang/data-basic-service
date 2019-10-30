package com.han.datamgr.vo;

/**
 * @author: Hanl
 * @date :2019/9/27
 * @desc:
 */
public abstract class BaseVO<T> implements java.io.Serializable {

    public abstract T to();

    public abstract void from(T t);

}
