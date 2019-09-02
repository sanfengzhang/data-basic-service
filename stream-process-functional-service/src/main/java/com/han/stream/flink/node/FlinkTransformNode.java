package com.han.stream.flink.node;

import com.han.stream.flink.OperatorEnum;
import com.han.stream.flink.function.DefaultTransformFunction;
import com.han.stream.flink.support.Message;
import com.stream.data.transform.model.CommandPipeline;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.streaming.api.datastream.DataStream;

import java.util.Map;

/**
 * @author: Hanl
 * @date :2019/8/20
 * @desc:
 */
@Slf4j
@Data
public class FlinkTransformNode extends AbstractFlinkNode {

    private static final long serialVersionUID = 3893838649799734961L;

    private OperatorEnum operatorEnum;

    private String transformContextName;

    private Map<String, CommandPipeline> commandPipelines;

    public FlinkTransformNode(String transformContextName, Map<String, CommandPipeline> commandPipelines) {
        this.transformContextName = transformContextName;
        this.commandPipelines = commandPipelines;
    }

    public static FlinkTransformNode buildDefaultTransformNode(String transformContextName, Map<String, CommandPipeline> commandPipelines) {

        return new FlinkTransformNode(transformContextName, commandPipelines);
    }

    public DataStream<Map<String, Object>> process(DataStream<Message> preDataStream) {

        return preDataStream.process(new DefaultTransformFunction(transformContextName, commandPipelines)).name(getDataProcessNodeName());
    }

}
