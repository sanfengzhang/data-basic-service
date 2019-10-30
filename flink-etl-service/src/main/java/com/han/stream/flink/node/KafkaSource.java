package com.han.stream.flink.node;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.han.stream.flink.JobConfigContext;
import com.han.stream.flink.support.DefaultKafkaDeserializationSchema;
import com.han.stream.flink.support.Message;
import lombok.Data;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer010;

import java.util.List;
import java.util.Map;
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

    public KafkaSource(JobConfigContext jobConfigContext) throws Exception {
        this.jobConfigContext = jobConfigContext;
        this.topics = JSON.parseObject(jobConfigContext.getString("flink.etl.kafka.topics"), new TypeReference<List<String>>() {
        });
        Map<String, Object> propsMap = JSON.parseObject(jobConfigContext.getString("flink.etl.kafka.props"), new TypeReference<Map<String, Object>>() {
        });
        this.props = new Properties();
        this.props.putAll(propsMap);
        this.charSet = jobConfigContext.getString("flink.etl.kafka.charset");
    }

    public DataStream<Message> getSource(StreamExecutionEnvironment env) {
        DataStream<Message> dataStreamSource;
        if (null == pattern) {
            dataStreamSource = env.addSource(new FlinkKafkaConsumer010<Message>(topics, new DefaultKafkaDeserializationSchema(charSet), props),
                    "FLINK-KAFKA-SOURCE");
        } else {
            dataStreamSource = env.addSource(new FlinkKafkaConsumer010<Message>(pattern, new DefaultKafkaDeserializationSchema(charSet), props),
                    "FLINK-KAFKA-SOURCE-PATTERN");
        }
        return dataStreamSource;
    }
}
