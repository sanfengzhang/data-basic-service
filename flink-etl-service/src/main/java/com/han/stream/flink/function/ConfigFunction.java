package com.han.stream.flink.function;

import com.han.stream.flink.config.ConfigParameters;
import com.han.stream.flink.support.Message;
import org.apache.flink.api.common.functions.MapFunction;

/**
 * @author: Hanl
 * @date :2019/10/30
 * @desc:
 */
public class ConfigFunction implements MapFunction<Message, ConfigParameters> {


    @Override
    public ConfigParameters map(Message value) throws Exception {
        return null;
    }
}
