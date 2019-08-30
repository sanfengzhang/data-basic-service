package com.han.stream.flink.function.transform;

import com.codahale.metrics.SharedMetricRegistries;
import com.han.stream.flink.exception.TransformException;
import com.han.stream.flink.function.Transform;
import com.han.stream.flink.support.CommonMessage;
import com.stream.data.transform.model.CommandPipeline;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.kitesdk.morphline.api.Command;
import org.kitesdk.morphline.api.MorphlineContext;
import org.kitesdk.morphline.api.Record;
import org.kitesdk.morphline.base.Compiler;
import org.kitesdk.morphline.base.FaultTolerance;
import org.kitesdk.morphline.base.Fields;
import org.kitesdk.morphline.base.Notifications;

import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: Hanl
 * @date :2019/8/30
 * @desc:
 */
public abstract class MorphlineTransform<OUT> implements Transform<CommonMessage, OUT> {

    private Map<String, CommandPipeline> commandPipelines;

    private Map<String, Command> commands = new ConcurrentHashMap<>();

    private Map<String, Collector> collectors = new ConcurrentHashMap<>();

    private String charset;


    private MorphlineContext morphlineContext;

    protected MorphlineTransform(String transformContextName, Map<String, CommandPipeline> commandPipelines, String charset) {

        this.commandPipelines = commandPipelines;
        this.charset = charset;

        morphlineContext = new MorphlineContext.Builder().setExceptionHandler(new FaultTolerance(false, false))
                .setMetricRegistry(SharedMetricRegistries.getOrCreate(transformContextName)).build();

        commandPipelines.forEach((dataType, commandPipeline) -> {
            Map<String, Object> commandMap = commandPipeline.get();
            Config config = ConfigFactory.parseMap(commandMap);
            Collector finalChild = new Collector();
            Command cmd = new Compiler().compile(config, morphlineContext, finalChild);
            commands.put(dataType, cmd);
            collectors.put(dataType, finalChild);
        });
    }

    @Override
    public Map<String, Collection<Object>> process(CommonMessage message) {
        String dataType = message.getType();
        Command cmd = commands.get(dataType);
        Collector finalChild = collectors.get(dataType);
        try {
            if (null == cmd) {
                throw new TransformException("Failed to transform record,because command null,dataType=" + dataType);
            }
            if (null == finalChild) {
                finalChild = new Collector();
                collectors.put(dataType, finalChild);
            }
            Record record = new Record();
            byte[] messageBytesBody = message.getValue().getBytes(charset);
            record.put(Fields.ATTACHMENT_BODY, messageBytesBody);
            Notifications.notifyStartSession(cmd);
            if (!cmd.process(record)) {

                throw new TransformException("Failed to transform record,dataType=" + message + ",message=" + message);
            }
            record = finalChild.getRecords().get(0);
            Map<String, Collection<Object>> result = record.getFields().asMap();
            return result;
        } catch (UnsupportedEncodingException e) {
            throw new TransformException("Unsupported encoding ", e);
        } finally {
            finalChild.reset();
            Notifications.notifyShutdown(cmd);
        }
    }
}
