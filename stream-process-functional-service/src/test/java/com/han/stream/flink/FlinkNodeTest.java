package com.han.stream.flink;


import com.han.stream.flink.node.FlinkSourceNode;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.junit.Before;
import org.junit.Test;

/**
 * @author: Hanl
 * @date :2019/8/20
 * @desc:
 */
public class FlinkNodeTest {

    StreamExecutionEnvironment env = null;

    @Before
    public void setup() {
        env = StreamExecutionEnvironment.getExecutionEnvironment();
        // job失败重启的策略
        env.getConfig().setRestartStrategy(RestartStrategies.fixedDelayRestart(3, 1000L));
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        env.disableOperatorChaining();

        // 设置合理的CP的时间也是需要考量的
        env.getCheckpointConfig().setCheckpointInterval(10000l);
        env.getCheckpointConfig().setCheckpointTimeout(5000L);
        env.setParallelism(2);

    }

    @Test
    public void testFlinkNode() {
        FlinkSourceNode sourceNode = new FlinkSourceNode();
        sourceNode.setOperatorEnum(OperatorEnum.SOURCE_KAFKA);

        FlinkSourceNode transformNode = new FlinkSourceNode();
        transformNode.setOperatorEnum(OperatorEnum.MAP);




    }

}
