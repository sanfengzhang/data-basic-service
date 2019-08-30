package com.han.stream.flink.function;

import com.han.stream.flink.exception.TransformException;
import com.han.stream.flink.function.transform.DefaultMorphlineTransform;
import com.han.stream.flink.support.CommonMessage;
import com.stream.data.transform.model.CommandPipeline;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.metrics.Counter;

import java.util.Collection;
import java.util.Map;

/**
 * @author: Hanl
 * @date :2019/8/20
 * @desc:
 */
@Slf4j
public class DefaultTransformFunction extends RichMapFunction<CommonMessage, Map<String, Object>> {

    private static final long serialVersionUID = 1L;

    public static final String TRANSFORM_SUCCEEDED_METRICS_COUNTER = "transformSuccess";

    public static final String TRANSFORM_FAILED_METRICS_COUNTER = "transformFailed";

    private transient Counter failedProcessRecordsNum;

    private transient Counter successProcessRecordsNum;

    private Transform<CommonMessage, Map<String, Object>> transform;

    private String transformContextName;

    private Map<String, CommandPipeline> commandPipelines;

    private String charset;

    public DefaultTransformFunction(String transformContextName, Map<String, CommandPipeline> commandPipelines, String charset) {
        this.charset = charset;
        this.commandPipelines = commandPipelines;
        this.transformContextName = transformContextName;
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        transform = new DefaultMorphlineTransform(transformContextName, commandPipelines, charset);
        this.successProcessRecordsNum = this.getRuntimeContext().getMetricGroup().counter(TRANSFORM_SUCCEEDED_METRICS_COUNTER);
        this.failedProcessRecordsNum = this.getRuntimeContext().getMetricGroup().counter(TRANSFORM_FAILED_METRICS_COUNTER);
    }

    @Override
    public Map<String, Object> map(CommonMessage message) throws Exception {
        Map<String, Object> result = null;
        try {
            Map<String, Collection<Object>> processResult = transform.process(message);
            result = transform.output(processResult);
            this.successProcessRecordsNum.inc();
        } catch (TransformException e) {
            this.failedProcessRecordsNum.inc();
            log.warn("Failed to transform message=[" + message + "].", e);
        }
        return result;
    }
}
