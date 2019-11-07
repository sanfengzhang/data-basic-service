package com.han.stream.flink.node;

import com.han.stream.flink.OperatorEnum;
import com.han.stream.flink.config.ConfigParameters;
import com.han.stream.flink.function.ConfigurableMorphlineTransformFunction;
import com.han.stream.flink.function.DefaultTransformFunction;
import com.han.stream.flink.support.Message;
import com.stream.data.transform.model.CommandPipeline;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.streaming.api.datastream.BroadcastStream;
import org.apache.flink.streaming.api.datastream.DataStream;

import java.util.List;
import java.util.Map;

/**
 * @author: Hanl
 * @date :2019/8/20
 * @desc:
 */
@Slf4j
@Data
public class FlinkTransformNode extends AbstractFlinkNode {

    private static final long serialVersionUID = 3893838649799734961L;

    private OperatorEnum operatorEnum;

    private String transformContextName;

    private boolean configurable;

    private DataStream connectDataStream;

    private BroadcastStream<ConfigParameters> broadcastStream;

    private List<Map<String,Object>> morphFlows;

    public FlinkTransformNode(String transformContextName,List<Map<String,Object>> morphFlows) {
        this(transformContextName, morphFlows, false);
    }

    public FlinkTransformNode(String transformContextName, List<Map<String,Object>> morphFlows, boolean configurable) {
        this.transformContextName = transformContextName;
        this.morphFlows = morphFlows;
        this.configurable = configurable;
    }

    public static FlinkTransformNode buildDefaultTransformNode(String transformContextName, List<Map<String,Object>> morphFlows, boolean configurable) {
        if (!configurable)
            return new FlinkTransformNode(transformContextName, morphFlows, false);
        else
            return new FlinkTransformNode(transformContextName, morphFlows, true);
    }


    public DataStream<Map<String, Object>> process(DataStream<Message> preDataStream) {
        if (!configurable)
            return preDataStream.process(new DefaultTransformFunction(transformContextName,"", morphFlows)).name(getDataProcessNodeName());
        else {

            return preDataStream.connect(broadcastStream).process(new ConfigurableMorphlineTransformFunction(transformContextName,"", morphFlows)).name(getDataProcessNodeName());
        }
    }

}
