package com.han.stream.flink;


import com.han.dataflow.api.model.AbstractDataProcessNode;
import com.han.stream.flink.node.FlinkSinkNode;
import com.han.stream.flink.node.FlinkSourceNode;
import com.han.stream.flink.node.FlinkTransformNode;
import com.stream.data.transform.api.CommandBuildService;
import com.stream.data.transform.model.CommandPipeline;
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
    public void testSocketFlinkBuildService() {
        FlinkBuildJobService flinkBuildJobService = new FlinkBuildJobService();

        FlinkSourceNode sourceNode = FlinkSourceNode.buildSocket("127.0.0.1", 8085, "test-type");
        sourceNode.setDataProcessNodeName("socket");
        FlinkTransformNode flinkTransformNode = FlinkTransformNode.buildDefaultTransformNode("Default Morphline Context",commandPipelineMap,"UTF-8");
        flinkTransformNode.setDataProcessNodeName("transform");
        FlinkSinkNode flinkSinkNode = FlinkSinkNode.buildPrintSink();

        List<AbstractDataProcessNode> list = new ArrayList<>();
        list.add(sourceNode);
        list.add(flinkTransformNode);
        list.add(flinkSinkNode);

        List<List<AbstractDataProcessNode>> result = new ArrayList<>();
        result.add(list);

        flinkBuildJobService.run(result, null);
    }
}
