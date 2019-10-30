package com.han.stream.flink.node;

import com.han.stream.flink.JobConfigContext;
import com.han.stream.flink.support.DefaultKafkaDeserializationSchema;
import com.han.stream.flink.support.Message;
import lombok.Data;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer010;

import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

/**
 * @author: Hanl
 * @date :2019/10/30
 * @desc:
 */
@Data
public class KafkaSource {

    private List<String> topics;

    private Properties props;

    private Pattern pattern;

    private String charSet;

    private JobConfigContext jobConfigContext;

    public KafkaSource(JobConfigContext jobConfigContext) {

    }

    public DataStream<Message> getSource(StreamExecutionEnvironment env) {
        DataStream<Message> dataStreamSource;
        if (null == pattern) {
            dataStreamSource = env.addSource(new FlinkKafkaConsumer010<Message>(topics, new DefaultKafkaDeserializationSchema(charSet), props),
                    "FLINK-KAFKA-SOURCE");
        } else {
            dataStreamSource = env.addSource(new FlinkKafkaConsumer010<Message>(pattern, new DefaultKafkaDeserializationSchema(charSet), props),
                    "FLINK-KAFKA-SOURCE");
        }
        return dataStreamSource;
    }
}
