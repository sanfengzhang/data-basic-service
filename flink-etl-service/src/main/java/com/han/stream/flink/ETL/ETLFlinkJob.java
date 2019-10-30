package com.han.stream.flink.ETL;

import com.han.stream.flink.BaseFlinkJob;
import com.han.stream.flink.config.ConfigParameters;
import com.han.stream.flink.function.ConfigFunction;
import com.han.stream.flink.function.ConfigurableMorphlineTransformFunction;
import com.han.stream.flink.node.SocketSource;
import com.han.stream.flink.support.Constants;
import com.han.stream.flink.support.Message;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class ETLFlinkJob extends BaseFlinkJob {

    public ETLFlinkJob() {
        log.info("-----------start etl work---------");
        this.initJobConfigContextByHttp("http://localhost:8080/ETL/job/config");
    }


    @Override
    public void run() throws Exception {
        Map<String, String> morphFlows = jobConfigContext.getCommandPipelineMap("");

        StreamExecutionEnvironment env = creatStreamExecutionEnvironment();


        DataStream<String> dataStreamSourceString = new SocketSource(jobConfigContext, "1").getSource(env);
        DataStream<Message> dataStreamSourceMessage = dataStreamSourceString.map(new MapFunction<String, Message>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Message map(String value) throws Exception {
                Message message = new Message("socket-type", value);
                return message;
            }
        });

        DataStream<ConfigParameters> configStream = new SocketSource(jobConfigContext, "2").getSource(env).map(new ConfigFunction());
        BroadcastStream<ConfigParameters> broadcastStream = configStream.broadcast(new MapStateDescriptor<>("config_descriptor", String.class, Map.class));

        SingleOutputStreamOperator<Map<String, Object>> mapDataStream = dataStreamSourceMessage.connect(broadcastStream).
                process(new ConfigurableMorphlineTransformFunction("Flink_Transform_Context", morphFlows));
        mapDataStream.getSideOutput(new OutputTag<Map<String, Object>>(Constants.FLINK_FAILED) {
            private static final long serialVersionUID = 1L;
        }).print();
        mapDataStream.getSideOutput(new OutputTag<Map<String, Object>>(Constants.UPDATE_CONFIG_PARAMETERS) {
            private static final long serialVersionUID = 1L;
        }).print();
        mapDataStream.print();
        env.execute();
    }

}
