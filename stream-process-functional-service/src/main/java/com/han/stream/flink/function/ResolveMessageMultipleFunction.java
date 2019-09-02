package com.han.stream.flink.function;

import com.han.stream.flink.support.Message;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;

/**
 * @author: Hanl
 * @date :2019/9/2
 * @desc:
 */
public class ResolveMessageMultipleFunction  extends ProcessFunction<Message,Message> {

    @Override
    public void processElement(Message value, Context ctx, Collector<Message> out) throws Exception {

    }
}
