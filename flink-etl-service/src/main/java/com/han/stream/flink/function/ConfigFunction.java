package com.han.stream.flink.function;

import com.han.stream.flink.config.ConfigParameters;
import org.apache.flink.api.common.functions.MapFunction;

/**
 * @author: Hanl
 * @date :2019/10/30
 * @desc:
 */
public class ConfigFunction implements MapFunction<String, ConfigParameters> {


    @Override
    public ConfigParameters map(String value) throws Exception {
        return null;
    }
}
