package com.han.stream.flink.node;

import com.google.common.base.Preconditions;
import com.han.stream.flink.OperatorEnum;
import com.han.stream.flink.function.DefaultTransformFunction;
import com.han.stream.flink.support.CommonMessage;
import com.stream.data.transform.model.CommandPipeline;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.functions.RichMapFunction;
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

    private String charset;

    private String transformContextName;

    private Map<String, CommandPipeline> commandPipelines;

    public FlinkTransformNode(String transformContextName, Map<String, CommandPipeline> commandPipelines, String charset) {
        this.charset = charset;
        this.transformContextName = transformContextName;
        this.commandPipelines = commandPipelines;
    }

    public static FlinkTransformNode buildDefaultTransformNode(String transformContextName, Map<String, CommandPipeline> commandPipelines, String charset) {

        return new FlinkTransformNode(transformContextName, commandPipelines, charset);
    }

    public DataStream<Map<String, Object>> map(DataStream<CommonMessage> preDataStream) {

        return preDataStream.map(new DefaultTransformFunction(transformContextName, commandPipelines, charset)).name(getDataProcessNodeName());
    }

}
