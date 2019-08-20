package com.han.stream.flink.function;

import com.codahale.metrics.SharedMetricRegistries;
import com.google.common.base.Preconditions;
import com.han.stream.flink.support.CommonMessage;
import com.stream.data.transform.model.CommandPipeline;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.metrics.Counter;
import org.kitesdk.morphline.api.Command;
import org.kitesdk.morphline.api.MorphlineContext;
import org.kitesdk.morphline.api.Record;
import org.kitesdk.morphline.base.Compiler;
import org.kitesdk.morphline.base.FaultTolerance;
import org.kitesdk.morphline.base.Fields;
import org.kitesdk.morphline.base.Notifications;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: Hanl
 * @date :2019/8/20
 * @desc:
 */
public abstract class AbstractTransformFunction<OUT> extends RichMapFunction<CommonMessage, OUT>
        implements Transform<CommonMessage, OUT> {

    private static final long serialVersionUID = 1L;

    private Map<String, CommandPipeline> dataTypeToCommandPipeline = null;

    private Map<String, Command> dataTypeToCommand = null;

    private Map<String, Collector> dataTypeToCollector = null;

    private MorphlineContext morphlineContext = null;

    public static final String TRANSFORM_SUCCEEDED_METRICS_COUNTER = "transformSuccess";

    public static final String TRANSFORM_FAILED_METRICS_COUNTER = "transformFailed";

    private transient Counter failedProcessRecordsNum;

    private transient Counter successProcessRecordsNum;

    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractTransformFunction.class);

    public AbstractTransformFunction(Map<String, CommandPipeline> dataTypeToCommandPipeline) {

        this.dataTypeToCommandPipeline = dataTypeToCommandPipeline;
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        this.successProcessRecordsNum = this.getRuntimeContext().getMetricGroup().counter(TRANSFORM_SUCCEEDED_METRICS_COUNTER);
        this.failedProcessRecordsNum = this.getRuntimeContext().getMetricGroup().counter(TRANSFORM_FAILED_METRICS_COUNTER);

        morphlineContext = new MorphlineContext.Builder().setExceptionHandler(new FaultTolerance(false, false))
                .setMetricRegistry(SharedMetricRegistries.getOrCreate("Flink_transformContext")).build();

        dataTypeToCommand = new ConcurrentHashMap<>();
        dataTypeToCommandPipeline.forEach((dataType, commands) -> {
            Map<String, Object> configMap = commands.get();
            Config config = ConfigFactory.parseMap(configMap);
            Collector finalChild = new Collector();
            Command cmd = new Compiler().compile(config, morphlineContext, finalChild);
            dataTypeToCommand.put(dataType, cmd);
            dataTypeToCollector.put(dataType, finalChild);
            Notifications.notifyStartSession(cmd);
        });
    }

    @Override
    public OUT map(CommonMessage message) throws Exception {

        Map<String, Collection<Object>> processResult = process(message);
        return output(processResult);
    }

    @Override
    public Map<String, Collection<Object>> process(CommonMessage message) {
        String dataType = message.getType();
        Command cmd = dataTypeToCommand.get(dataType);
        Collector finalChild = dataTypeToCollector.get(dataType);
        try {
            if (null == cmd) {
                throw new NullPointerException("Transform command must not be null,message type=" + dataType);
            }
            if (null == finalChild) {
                finalChild = new Collector();
                dataTypeToCollector.put(dataType, finalChild);
            }

            Record record = new Record();
            record.put(Fields.ATTACHMENT_BODY, message.getValue().getBytes("UTF-8"));
            Notifications.notifyStartSession(cmd);
            if (!cmd.process(record)) {
                throw new RuntimeException("Failed to transform record");
            }

            record = finalChild.getRecords().get(0);
            Map<String, Collection<Object>> result = record.getFields().asMap();
            successProcessRecordsNum.inc();
            return result;
        } catch (Exception e) {
            failedProcessRecordsNum.inc();
            LOGGER.warn("Transform {} failed to process record: {},exception cause={}", message.toString(), e.getCause());
        } finally {
            finalChild.reset();
        }
        return null;
    }

    @Override
    public void close() throws Exception {

        if (null != dataTypeToCommand && !dataTypeToCommand.isEmpty()) {
            for (Command cmd : dataTypeToCommand.values()) {
                Notifications.notifyShutdown(cmd);
            }
            dataTypeToCommand = null;
        }
    }


    public static final class Collector implements Command {

        private final List<Record> results = new ArrayList<Record>();

        public List<Record> getRecords() {
            return results;
        }

        public void reset() {
            results.clear();
        }

        public Command getParent() {
            return null;
        }

        public void notify(Record notification) {
        }

        public boolean process(Record record) {
            Preconditions.checkNotNull(record);
            results.add(record);
            return true;
        }

    }
}
