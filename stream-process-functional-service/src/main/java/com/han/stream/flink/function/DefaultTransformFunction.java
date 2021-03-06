package com.han.stream.flink.function;

import com.han.stream.flink.exception.TransformException;
import com.han.stream.flink.function.transform.DefaultMorphlineTransform;
import com.han.stream.flink.support.Constants;
import com.han.stream.flink.support.Message;
import com.stream.data.transform.model.CommandPipeline;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.metrics.Counter;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: Hanl
 * @date :2019/8/20
 * @desc:
 */
@Slf4j
public class DefaultTransformFunction extends ProcessFunction<Message, Map<String, Object>> {

    private static final long serialVersionUID = 1L;

    public static final String TRANSFORM_SUCCEEDED_METRICS_COUNTER = "transformSuccess";

    public static final String TRANSFORM_FAILED_METRICS_COUNTER = "transformFailed";

    private transient Counter failedProcessRecordsNum;

    private transient Counter successProcessRecordsNum;

    private Transform<Message, Map<String, Object>> transform;

    private String transformContextName;

    private Map<String, CommandPipeline> commandPipelines;

    private OutputTag<Map<String, Object>> failedTag = new OutputTag<Map<String, Object>>(Constants.FLINK_FAILED) {
    };

    public DefaultTransformFunction(String transformContextName, Map<String, CommandPipeline> commandPipelines) {
        this.commandPipelines = commandPipelines;
        this.transformContextName = transformContextName;
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        transform = new DefaultMorphlineTransform(transformContextName, commandPipelines);
        this.successProcessRecordsNum = this.getRuntimeContext().getMetricGroup().counter(TRANSFORM_SUCCEEDED_METRICS_COUNTER);
        this.failedProcessRecordsNum = this.getRuntimeContext().getMetricGroup().counter(TRANSFORM_FAILED_METRICS_COUNTER);
    }

    @Override
    public void processElement(Message message, Context ctx, Collector<Map<String, Object>> out) throws Exception {
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
}
