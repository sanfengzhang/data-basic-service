package com.han.stream.flink;

import com.alibaba.fastjson.JSON;
import com.han.stream.flink.config.ConfigParameters;
import com.stream.data.transform.api.CommandBuildService;
import com.stream.data.transform.model.CommandPipeline;
import org.apache.flink.api.common.functions.MapFunction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Hanl
 * @date :2019/9/5
 * @desc:
 */
public class ConfigFunction implements MapFunction<String, ConfigParameters> {

    private static final long serialVersionUID = 1L;


    @Override
    public ConfigParameters map(String value) throws Exception {
        ConfigParameters configParameters = new ConfigParameters();
        if ("1".equals(value)) {
            Map<String, Object> readLineMap = new HashMap<>();
            Map<String, Object> charsetMap = new HashMap<>();
            charsetMap.put("charset", "UTF-8");
            readLineMap.put("readLine", charsetMap);
            List<String> outputFields = new ArrayList<>();
            outputFields.add("trans_date");
            outputFields.add("trans_channel_id");
            outputFields.add("trans_ip");

            Map<String, Object> splitCommand = CommandBuildService.spilt("message", outputFields, "|", false, false, false, 3);

            Map<String, String> expressMap = new HashMap<>();
            expressMap.put("trans_channel_id<0 \"?\" 99999 \":\"trans_return_code", "java.lang.Integer,trans_channel_id_example");
            Map<String, Object> cacheWarmingData = new HashMap<>();
            cacheWarmingData.put("trans_channel_id", "999");
            Map<String, Object> expressCommand = CommandBuildService.elExpress(expressMap, cacheWarmingData);

            List<String> imports = new ArrayList<>();
            imports.add("com.stream.data.transform.command.*");
            CommandPipeline commands = CommandPipeline.build("trad_conf_test", imports).addCommand(splitCommand).addCommand(expressCommand);
            configParameters .put("test1", commands);

        }
        return configParameters;
    }
}
