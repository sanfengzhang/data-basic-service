package com.han.stream.flink.node;

import com.han.stream.flink.OperatorEnum;
import org.apache.flink.streaming.api.datastream.DataStream;

import java.util.Map;

/**
 * @author: Hanl
 * @date :2019/8/20
 * @desc:
 */
public class FlinkSinkNode extends AbstractFlinkNode {

    private static final long serialVersionUID = 1793687255516466473L;

    private OperatorEnum operatorEnum;

    public void sink(DataStream<Map<String, Object>> dataStream) {

        dataStream.print();
    }
}
