package com.han.stream.flink.function.transform;

import com.codahale.metrics.SharedMetricRegistries;
import com.han.stream.flink.config.ConfigParameters;
import com.han.stream.flink.exception.TransformException;
import com.han.stream.flink.function.Transform;
import com.han.stream.flink.support.Message;
import com.stream.data.transform.model.CommandPipeline;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.kitesdk.morphline.api.Command;
import org.kitesdk.morphline.api.MorphlineContext;
import org.kitesdk.morphline.api.Record;
import org.kitesdk.morphline.base.Compiler;
import org.kitesdk.morphline.base.FaultTolerance;
import org.kitesdk.morphline.base.Fields;
import org.kitesdk.morphline.base.Notifications;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: Hanl
 * @date :2019/8/30
 * @desc:
 */
@Data
@Slf4j
public abstract class MorphlineTransform<OUT> implements Transform<Message, OUT> {

    //FIXME 在Flink流式处理中不存在这种并发操作的问题
    private Map<String, Command> commands = new ConcurrentHashMap<>();

    private Map<String, Collector> collectors = new ConcurrentHashMap<>();

    private MorphlineContext morphlineContext;

    protected MorphlineTransform(String transformContextName, Map<String, CommandPipeline> commandPipelines) {
        morphlineContext = new MorphlineContext.Builder().setExceptionHandler(new FaultTolerance(false, false))
                .setMetricRegistry(SharedMetricRegistries.getOrCreate(transformContextName)).build();
        Compiler compiler=new Compiler();
        commandPipelines.forEach((dataType, commandPipeline) -> {
            Map<String, Object> commandMap = commandPipeline.get();
            Config config = ConfigFactory.parseMap(commandMap);
            Collector finalChild = new Collector();
            Command cmd = compiler.compile(config, morphlineContext, finalChild);
            commands.put(dataType, cmd);
            collectors.put(dataType, finalChild);
        });
    }

    @Override
    public Map<String, Collection<Object>> process(Message message) {
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

            //FIXME 这里目前就是支持String类型的数据
            record.put(Fields.MESSAGE, message.getValue());
            Notifications.notifyStartSession(cmd);
            if (!cmd.process(record)) {

                throw new TransformException("Failed to process record,dataType=" + message + ",message=" + message);
            }
            record = finalChild.getRecords().get(0);
            Map<String, Collection<Object>> result = record.getFields().asMap();
            return result;
        } catch (Exception e) {
            throw new TransformException("Failed to transform Exception ", e);
        } finally {
            if (null != finalChild) {
                finalChild.reset();
            }
            if (null != cmd) {
                Notifications.notifyShutdown(cmd);
            }
        }
    }

    /**
     * FIXME Update or add command
     * 1.实现Command配置更新可以自己去实现配置方式、发布或订阅或者定时任务去获取变化
     * 2.通过Flink的{@BroadcastProcessFunction}的方式去下发变化
     *
     * @param configParameters
     */
    public void updateOrAddCommand(ConfigParameters configParameters) {
        configParameters.getConfig().forEach((dataType, commandPipelineObj) -> {
            Map<String, Object> commandMap = null;
            CommandPipeline commandPipeline = null;
            if (commandPipelineObj instanceof CommandPipeline) {
                commandPipeline = (CommandPipeline) commandPipelineObj;
                commandMap = commandPipeline.get();
            }
            Config config = ConfigFactory.parseMap(commandMap);
            Collector finalChild = new Collector();
            Command cmd = new Compiler().compile(config, morphlineContext, finalChild);
            commands.put(dataType, cmd);
            collectors.put(dataType, finalChild);
        });
        log.info("Update morphline config success,config={}", configParameters);
    }
}
