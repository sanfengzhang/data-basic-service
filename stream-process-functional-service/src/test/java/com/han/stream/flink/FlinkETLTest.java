package com.han.stream.flink;

import com.han.stream.flink.config.ConfigParameters;
import com.han.stream.flink.function.ConfigurableMorphlineTransformFunction;
import com.han.stream.flink.function.DefaultTransformFunction;
import com.han.stream.flink.support.Constants;
import com.han.stream.flink.support.Message;
import com.stream.data.transform.api.CommandBuildService;
import com.stream.data.transform.model.CommandPipeline;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.streaming.api.datastream.BroadcastStream;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.OutputTag;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Hanl
 * @date :2019/9/5
 * @desc:
 */
public class FlinkETLTest {

    HashMap<String, CommandPipeline> commandPipelineMap = new HashMap<>();

    CommandPipeline commands=null;

    @Before
    public void setup() {
        System.out.println(null instanceof  Map);
        Map<String, Object> readLineMap = new HashMap<>();
        Map<String, Object> charsetMap = new HashMap<>();
        charsetMap.put("charset", "UTF-8");
        readLineMap.put("readLine", charsetMap);
        List<String> outputFields = new ArrayList<>();
        outputFields.add("trans_date");
        outputFields.add("trans_channel_id");
        outputFields.add("trans_ip");

        Map<String, Object> splitCommand = CommandBuildService.spilt("message", outputFields, "|", false, false, false, 3);

        Map<String, String> expressMap = new HashMap<>();
        expressMap.put("trans_channel_id<0 \"?\" 99999 \":\"trans_return_code", "java.lang.Integer,trans_channel_id_example");
        Map<String, Object> cacheWarmingData = new HashMap<>();
        cacheWarmingData.put("trans_channel_id", "999");
        Map<String, Object> expressCommand = CommandBuildService.elExpress(expressMap, cacheWarmingData);

        List<String> imports = new ArrayList<>();
        imports.add("com.stream.data.transform.command.*");
        commands = CommandPipeline.build("trad_conf", imports).addCommand(splitCommand).addCommand(expressCommand);
        commandPipelineMap.put("test-type", commands);
    }



    @Test
    public void testSocketFlink() throws Exception {
        FlinkBuildJobService flinkBuildJobService = new FlinkBuildJobService();
        StreamExecutionEnvironment env = flinkBuildJobService.creatStreamExecutionEnvironment(null);
        DataStream<String> dataStreamSourceTmp = env.socketTextStream("127.0.0.1", 8085).name("Socket Source");
        DataStream<Message> dataStreamSource = dataStreamSourceTmp.map(new MapFunction<String, Message>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Message map(String value) throws Exception {
                Message message = new Message("test-type", value);
                return message;
            }
        });
        SingleOutputStreamOperator<Map<String, Object>> mapDataStream = dataStreamSource.process(new DefaultTransformFunction("Flink_Transform_Context", commandPipelineMap));
        mapDataStream.getSideOutput(new OutputTag<Map<String, Object>>(Constants.FLINK_FAILED) {
        }).print();
        mapDataStream.print();
        env.execute();
    }

    @Test
    public void testSocketFlinkForConig() throws Exception {
        FlinkBuildJobService flinkBuildJobService = new FlinkBuildJobService();
        StreamExecutionEnvironment env = flinkBuildJobService.creatStreamExecutionEnvironment(null);
        DataStream<String> dataStreamSourceTmp = env.socketTextStream("127.0.0.1", 8085).name("Socket Source");
        DataStream<Message> dataStreamSource = dataStreamSourceTmp.map(new MapFunction<String, Message>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Message map(String value) throws Exception {
                Message message = new Message("test-type", value);
                return message;
            }
        });

        DataStream<ConfigParameters> configEventStream = env.socketTextStream("127.0.0.1", 8086).name("Socket Source Config").map(new ConfigFunction());


        BroadcastStream<ConfigParameters> configEventStream1 = configEventStream.broadcast(new MapStateDescriptor<>("config_descriptor", String.class, Map.class));
        SingleOutputStreamOperator<Map<String, Object>> mapDataStream = dataStreamSource.connect(configEventStream1).
                process(new ConfigurableMorphlineTransformFunction("Flink_Transform_Context", commandPipelineMap));
        mapDataStream.getSideOutput(new OutputTag<Map<String, Object>>(Constants.FLINK_FAILED) {
        }).print();
        mapDataStream.getSideOutput(new OutputTag<Map<String, Object>>(Constants.UPDATE_CONFIG_PARAMETERS) {
        }).print();
        mapDataStream.print();

        env.execute();
    }
}
