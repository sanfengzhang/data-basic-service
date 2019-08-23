package com.han.stream.flink.node;

import com.han.stream.flink.OperatorEnum;
import com.han.stream.flink.support.CommonMessage;
import com.han.stream.flink.support.DefaultKafkaDeserializationSchema;
import lombok.Data;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer010;

import java.util.List;
import java.util.Properties;

/**
 * @author: Hanl
 * @date :2019/8/9
 * @desc:
 */
@Data
public class FlinkSourceNode extends AbstractFlinkNode {

    private static final long serialVersionUID = -5559592007464859647L;

    private int parallelism;

    private OperatorEnum operatorEnum;

    private List<String> topics;

    private Properties props;

    private String host;

    private int port;

    private String dataType;

    public FlinkSourceNode() {

    }

    public static FlinkSourceNode buildSocket(String host, int port, String dataType) {
        FlinkSourceNode flinkSourceNode = new FlinkSourceNode();
        flinkSourceNode.setHost(host);
        flinkSourceNode.setPort(port);
        flinkSourceNode.setDataType(dataType);
        flinkSourceNode.setOperatorEnum(OperatorEnum.SOURCE_SOCKET);
        return flinkSourceNode;
    }

    public DataStream<CommonMessage> source(StreamExecutionEnvironment env) {
        DataStream<CommonMessage> dataStreamSource;
        if (operatorEnum == OperatorEnum.SOURCE_KAFKA) {
            dataStreamSource = env.addSource(new FlinkKafkaConsumer010(topics, new DefaultKafkaDeserializationSchema(), props),
                    "FLINK-KAFKA-SOURCE").name(getDataProcessNodeName());
            return dataStreamSource;
        } else if (operatorEnum == OperatorEnum.SOURCE_SOCKET) {
            DataStream<String> dataStreamSourceTmp = env.socketTextStream(host, port).name(getDataProcessNodeName());
            dataStreamSource = dataStreamSourceTmp.map(new MapFunction<String, CommonMessage>() {
                private static final long serialVersionUID = 1L;

                @Override
                public CommonMessage map(String value) throws Exception {
                    CommonMessage message = new CommonMessage(dataType, value);
                    return message;
                }
            });
            return dataStreamSource;
        }
        throw new UnsupportedOperationException("传递的算子操作类型不支持，operatorEnum=" + operatorEnum);
    }

}
