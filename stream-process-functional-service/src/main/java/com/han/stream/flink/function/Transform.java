package com.han.stream.flink.function;

import com.han.stream.flink.exception.TransformException;
import com.han.stream.flink.support.CommonMessage;

import java.util.Collection;
import java.util.Map;

/**
 * @author: Hanl
 * @date :2019/8/20
 * @desc:
 */
public interface Transform<IN extends CommonMessage, OUT> {

    /**
     *定义数据转换的主要接口方法，所有的数据转换操作均是在此方法中执行
     * @param commonMessage
     * @return
     */
    public Map<String, Collection<Object>> process(CommonMessage commonMessage);


    /**
     * 可以自己根据结果类型进行转换了，向流处理下游节点类型转换
     * @param value
     * @return
     */
    public OUT output(Map<String, Collection<Object>> value);
}
