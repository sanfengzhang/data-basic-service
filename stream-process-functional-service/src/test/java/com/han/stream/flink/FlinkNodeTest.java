package com.han.stream.flink;


import com.han.dataflow.api.model.AbstractDataProcessNode;
import com.han.stream.flink.node.FlinkSinkNode;
import com.han.stream.flink.node.FlinkSourceNode;
import com.han.stream.flink.node.FlinkTransformNode;
import com.han.stream.flink.support.CommonMessage;
import com.stream.data.transform.api.CommandBuildService;
import com.stream.data.transform.model.CommandPipeline;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Hanl
 * @date :2019/8/20
 * @desc:
 */
public class FlinkNodeTest {

    Map<String, CommandPipeline> commandPipelineMap = new HashMap<>();

    @Before
    public void setup() {

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
        CommandPipeline commands = CommandPipeline.build("trad_conf", imports).addCommand(readLineMap).addCommand(splitCommand).addCommand(expressCommand);
        commandPipelineMap.put("test-type", commands);
    }

    @Test
    public void testFlinkNode() throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        // job失败重启的策略
        env.getConfig().setRestartStrategy(RestartStrategies.fixedDelayRestart(3, 1000L));
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        env.disableOperatorChaining();

        // 设置合理的CP的时间也是需要考量的
        env.getCheckpointConfig().setCheckpointInterval(10000l);
        env.getCheckpointConfig().setCheckpointTimeout(5000L);
        env.setParallelism(2);

        FlinkSourceNode sourceNode = FlinkSourceNode.buildSocket("127.0.0.1", 8085, "test-type");
        sourceNode.setDataProcessNodeName("socket");
        FlinkTransformNode flinkTransformNode = new FlinkTransformNode(commandPipelineMap);
        flinkTransformNode.setDataProcessNodeName("transform");
        FlinkSinkNode flinkSinkNode = new FlinkSinkNode();
        DataStream<CommonMessage> dataStream = sourceNode.source(env);
        DataStream<Map<String, Object>> mapDataStream = flinkTransformNode.map(dataStream);
        flinkSinkNode.sink(mapDataStream);
        env.execute();
    }

    @Test
    public void testFlinkBuildService() {
        FlinkBuildJobService flinkBuildJobService = new FlinkBuildJobService();

        FlinkSourceNode sourceNode = FlinkSourceNode.buildSocket("127.0.0.1", 8085, "test-type");
        sourceNode.setDataProcessNodeName("socket");
        FlinkTransformNode flinkTransformNode = new FlinkTransformNode(commandPipelineMap);
        flinkTransformNode.setDataProcessNodeName("transform");
        FlinkSinkNode flinkSinkNode = new FlinkSinkNode();

        List<AbstractDataProcessNode> list = new ArrayList<>();
        list.add(sourceNode);
        list.add(flinkTransformNode);
        list.add(flinkSinkNode);

        List<List<AbstractDataProcessNode>> result = new ArrayList<>();
        result.add(list);

        flinkBuildJobService.run(result, null);
    }
}
