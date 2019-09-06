package com.han.stream.flink.node;

import com.han.stream.flink.support.Message;
import lombok.Data;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.util.OutputTag;

/**
 * @author: Hanl
 * @date :2019/9/4
 * @desc:
 */
@Data
public class FlinkOutputNode extends AbstractFlinkNode {

    private String outputTag;

    public FlinkOutputNode(String outputTag) {
        this.outputTag = outputTag;
    }

    public DataStream outputTag(DataStream<Message> preDataStream) {
        if (!(preDataStream instanceof SingleOutputStreamOperator)) {
            throw new ClassCastException("preDataStream can not cast SingleOutputStreamOperator");
        }
        SingleOutputStreamOperator singleOutputStreamOperator = (SingleOutputStreamOperator) preDataStream;
        return singleOutputStreamOperator.getSideOutput(new OutputTag(outputTag));
    }

}
