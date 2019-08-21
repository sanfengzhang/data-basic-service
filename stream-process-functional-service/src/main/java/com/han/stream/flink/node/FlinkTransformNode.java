package com.han.stream.flink.node;

import com.google.common.base.Preconditions;
import com.han.dataflow.api.model.AbstractDataProcessNode;
import com.han.stream.flink.function.DefaultTransformFunction;
import com.han.stream.flink.support.CommonMessage;
import com.stream.data.transform.model.CommandPipeline;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.streaming.api.datastream.DataStream;

import java.util.Map;

/**
 * @author: Hanl
 * @date :2019/8/20
 * @desc:
 */
@Slf4j
public class FlinkTransformNode extends AbstractFlinkNode {

    private static final long serialVersionUID = 3893838649799734961L;


    public Map<String, CommandPipeline> commandPipelineMap;

    public FlinkTransformNode(Map<String, CommandPipeline> commandPipelineMap) {
        Preconditions.checkNotNull(commandPipelineMap, "Commands must not be null.");
        this.commandPipelineMap = commandPipelineMap;
    }

    public DataStream<Map<String, Object>> map(DataStream<CommonMessage> preDataStream) {

        return preDataStream.map(new DefaultTransformFunction(commandPipelineMap)).name(getDataProcessNodeName());
    }

}
