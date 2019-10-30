package com.han.stream.flink.function;

import com.han.stream.flink.config.ConfigParameters;
import com.han.stream.flink.exception.TransformException;
import com.han.stream.flink.function.transform.DefaultMorphlineTransform;
import com.han.stream.flink.function.transform.MorphlineTransform;
import com.han.stream.flink.support.Constants;
import com.han.stream.flink.support.Message;
import com.stream.data.transform.model.CommandPipeline;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.metrics.Counter;
import org.apache.flink.streaming.api.functions.co.BroadcastProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: Hanl
 * @date :2019/9/2
 * @desc:
 */
@Slf4j
public class ConfigurableMorphlineTransformFunction extends BroadcastProcessFunction<Message, ConfigParameters, Map<String, Object>> {

    private transient Counter failedProcessRecordsNum;

    private transient Counter successProcessRecordsNum;

    private MorphlineTransform<Map<String, Object>> transform;

    private String transformContextName;

    private Map<String, CommandPipeline> commandPipelinesParams;

    private OutputTag<Map<String, Object>> failedTag = new OutputTag<Map<String, Object>>(Constants.FLINK_FAILED) {
    };

    private OutputTag<Map<String, Object>> updateConfigResponseTag = new OutputTag<Map<String, Object>>(Constants.FLINK_FAILED) {
    };

    public ConfigurableMorphlineTransformFunction(String transformContextName, Map<String, CommandPipeline> commandPipelines) {
        this.transformContextName = transformContextName;
        this.commandPipelinesParams = commandPipelines;
    }


    @Override
    public void open(Configuration parameters) throws Exception {

        transform = new DefaultMorphlineTransform(transformContextName, commandPipelinesParams);
        this.successProcessRecordsNum = this.getRuntimeContext().getMetricGroup().counter(DefaultTransformFunction.TRANSFORM_SUCCEEDED_METRICS_COUNTER);
        this.failedProcessRecordsNum = this.getRuntimeContext().getMetricGroup().counter(DefaultTransformFunction.TRANSFORM_FAILED_METRICS_COUNTER);
    }


    @Override
    public void processElement(Message message, ReadOnlyContext ctx, Collector<Map<String, Object>> out) throws Exception {
        Map<String, Object> result = new HashMap<>();
        try {
            Map<String, Collection<Object>> processResult = transform.process(message);
            result = transform.output(processResult);
            out.collect(result);
            this.successProcessRecordsNum.inc();
        } catch (TransformException e) {
            log.warn("Failed to transform message=[" + message + "].", e);
            this.failedProcessRecordsNum.inc();
            result.put(Constants.FLINK_FAILED_MSG, message);
            result.put(Constants.FLINK_FAILED_REASON, e.getCause());
            ctx.output(failedTag, result);
        }
    }

    @Override
    public void processBroadcastElement(ConfigParameters value, Context ctx, Collector<Map<String, Object>> out) throws Exception {
        Map<String, Object> result = new HashMap<>();
        try {
            log.info("Start updating the command information,commands={}", value);
            transform.updateOrAddCommand(value);
            result.put(Constants.UPDATE_CONFIG_PARAMETERS, value);
            result.put(Constants.UPDATE_CONFIG_PARAMETERS_STATUS, "true");
            result.put(Constants.UPDATE_CONFIG_PARAMETERS_EXECUTE_DATE, new Date());
        } catch (Exception e) {
            log.warn("Failed to update transform config, config=[" + value + "].", e);
            result.put(Constants.UPDATE_CONFIG_PARAMETERS, value);
            result.put(Constants.UPDATE_CONFIG_PARAMETERS_STATUS, e.getCause());
            result.put(Constants.UPDATE_CONFIG_PARAMETERS_EXECUTE_DATE, new Date());
        }
        ctx.output(updateConfigResponseTag, result);
    }
}
