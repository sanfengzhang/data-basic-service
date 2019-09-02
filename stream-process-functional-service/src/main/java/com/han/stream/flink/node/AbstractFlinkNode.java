package com.han.stream.flink.node;

import com.han.dataflow.api.model.AbstractDataProcessNode;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author: Hanl
 * @date :2019/8/21
 * @desc:
 */
public abstract class AbstractFlinkNode extends AbstractDataProcessNode {

    private static final long serialVersionUID = 308617118649940476L;

    private int parallelism;

    public DataStream handle(StreamExecutionEnvironment env, DataStream preDataStream) {
        if (this instanceof FlinkSourceNode) {
            FlinkSourceNode flinkSourceNode = (FlinkSourceNode) this;
            return flinkSourceNode.source(env);
        } else if (this instanceof FlinkTransformNode) {
            FlinkTransformNode flinkTransformNode = (FlinkTransformNode) this;
            return flinkTransformNode.process(preDataStream);
        } else if (this instanceof FlinkSinkNode) {
            FlinkSinkNode flinkSinkNode = (FlinkSinkNode) this;
            flinkSinkNode.sink(preDataStream, env);
        }

        return null;
    }
}
