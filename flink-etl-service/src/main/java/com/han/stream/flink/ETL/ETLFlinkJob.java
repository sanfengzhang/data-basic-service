package com.han.stream.flink.ETL;

import com.han.stream.flink.BaseFlinkJob;
import com.han.stream.flink.config.ConfigParameters;
import com.han.stream.flink.function.ConfigFunction;
import com.han.stream.flink.function.ConfigurableMorphlineTransformFunction;
import com.han.stream.flink.support.Constants;
import com.han.stream.flink.support.Message;
import com.stream.data.transform.model.CommandPipeline;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.streaming.api.datastream.BroadcastStream;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.OutputTag;

import java.util.Map;

/**
 * @author: Hanl
 * @date :2019/10/29
 * @desc:
 */
public class ETLFlinkJob extends BaseFlinkJob {

    public ETLFlinkJob() {

        this.initJobConfigContextByHttp("");
    }


    @Override
    public void run() throws Exception {
        Map<String, CommandPipeline> commandPipelineMap = jobConfigContext.getCommandPipelineMap("");

        StreamExecutionEnvironment env = creatStreamExecutionEnvironment();
        DataStream<String> dataStreamSource = env.socketTextStream(jobConfigContext.getString(""), jobConfigContext.getInt("")).name("Socket Source");

        DataStream<Message> dataStreamSourceMessage = dataStreamSource.map(new MapFunction<String, Message>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Message map(String value) throws Exception {
                Message message = new Message("test-type", value);
                return message;
            }
        });

        DataStream<ConfigParameters> configEventStream = env.socketTextStream(jobConfigContext.getString(""), jobConfigContext.getInt("")).name("Socket Source Config").map(new ConfigFunction());
        BroadcastStream<ConfigParameters> configEventStream1 = configEventStream.broadcast(new MapStateDescriptor<>("config_descriptor", String.class, Map.class));

        SingleOutputStreamOperator<Map<String, Object>> mapDataStream = dataStreamSourceMessage.connect(configEventStream1).
                process(new ConfigurableMorphlineTransformFunction("Flink_Transform_Context", commandPipelineMap));
        mapDataStream.getSideOutput(new OutputTag<Map<String, Object>>(Constants.FLINK_FAILED) {
        }).print();
        mapDataStream.getSideOutput(new OutputTag<Map<String, Object>>(Constants.UPDATE_CONFIG_PARAMETERS) {
        }).print();
        mapDataStream.print();
        env.execute();
    }

}
