package com.han.stream.flink.node;

import com.han.stream.flink.JobConfigContext;
import com.han.stream.flink.support.DefaultKafkaDeserializationSchema;
import com.han.stream.flink.support.Message;
import lombok.Data;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer010;

/**
 * @author: Hanl
 * @date :2019/10/30
 * @desc:
 */
@Data
public class SocketSource {

    private String host;

    private int port;

    private String dataType;

    private JobConfigContext jobConfigContext;

    public SocketSource(JobConfigContext jobConfigContext) throws Exception {
        this.jobConfigContext = jobConfigContext;
        this.host = jobConfigContext.getString("flink.source.socket.host");
        this.port = jobConfigContext.getInt("flink.source.socket.port");
        this.dataType=jobConfigContext.getString("flink.source.socket.data_type");
    }

    public DataStream<Message> getSource(StreamExecutionEnvironment env) {
        DataStream<String> dataStreamSourceString = env.socketTextStream(host, port).name("SOCKET-SOURCE");
        DataStream<Message> dataStreamSourceMessage = dataStreamSourceString.map(new MapFunction<String, Message>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Message map(String value) throws Exception {
                Message message = new Message(dataType, value);
                return message;
            }
        });
        return dataStreamSourceMessage;
    }
}
