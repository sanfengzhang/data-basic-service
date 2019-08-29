package com.han.stream.flink.node;

import com.han.stream.flink.OperatorEnum;
import lombok.Data;
import org.apache.flink.api.common.ExecutionConfig;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.TypeInformationSerializationSchema;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer010;

import java.util.Map;
import java.util.Properties;

/**
 * @author: Hanl
 * @date :2019/8/20
 * @desc:
 */
@Data
public class FlinkSinkNode extends AbstractFlinkNode {

    private static final long serialVersionUID = 1793687255516466473L;

    private OperatorEnum operatorEnum;

    private Properties producerConfig;

    private String topicId;

    private boolean writeTimestampToKafka;

    public static FlinkSinkNode buildPrintSink() {
        FlinkSinkNode flinkSinkNode = new FlinkSinkNode();
        flinkSinkNode.setOperatorEnum(OperatorEnum.SINK_PRINT);
        return flinkSinkNode;
    }

    public static FlinkSinkNode buildKafkaSink(String topicId, Properties producerConfig) {
        FlinkSinkNode flinkSinkNode = new FlinkSinkNode();
        flinkSinkNode.setOperatorEnum(OperatorEnum.SINK_KAFKA);
        flinkSinkNode.setTopicId(topicId);
        flinkSinkNode.setProducerConfig(producerConfig);
        return flinkSinkNode;
    }


    public void sink(DataStream<Map<String, Object>> dataStream, StreamExecutionEnvironment executionEnvironment) {

        if (operatorEnum == OperatorEnum.SINK_PRINT) {
            dataStream.print();
        } else if (operatorEnum == OperatorEnum.SINK_KAFKA) {
            TypeInformation<Tuple2<String, Map<String, Object>>> stringMapInfo = TypeInformation.of(new TypeHint<Tuple2<String, Map<String, Object>>>() {
            });
            ExecutionConfig config = executionEnvironment.getConfig();
            TypeInformationSerializationSchema<Tuple2<String, Map<String, Object>>> serSchema =
                    new TypeInformationSerializationSchema<Tuple2<String, Map<String, Object>>>(stringMapInfo, config);
            FlinkKafkaProducer010<Tuple2<String, Map<String, Object>>> flinkKafkaProducer010 =
                    new FlinkKafkaProducer010<Tuple2<String, Map<String, Object>>>(topicId, serSchema, producerConfig);
            flinkKafkaProducer010.setWriteTimestampToKafka(writeTimestampToKafka);

            dataStream.map(new MapFunction<Map<String, Object>, Tuple2<String, Map<String, Object>>>() {
                @Override
                public Tuple2<String, Map<String, Object>> map(Map<String, Object> value) throws Exception {
                    return new Tuple2<>(topicId, value);
                }
            }).addSink(flinkKafkaProducer010);

        } else {
            throw new UnsupportedOperationException("传递的算子操作类型不支持，operatorEnum=" + operatorEnum);
        }
    }
}
