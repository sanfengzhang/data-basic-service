package com.han.stream.flink;

import com.han.dataflow.api.model.AbstractDataProcessNode;
import com.han.stream.BuildJobService;
import com.han.stream.flink.node.AbstractFlinkNode;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.List;
import java.util.Map;

/**
 * @author: Hanl
 * @date :2019/8/7
 * @desc:
 */
public class FlinkBuildJobService implements BuildJobService {


    public StreamExecutionEnvironment creatStreamExecutionEnvironment() {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // job失败重启的策略
        env.getConfig().setRestartStrategy(RestartStrategies.fixedDelayRestart(3, 1000L));
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        env.disableOperatorChaining();

        // 设置合理的CP的时间也是需要考量的
        env.getCheckpointConfig().setCheckpointInterval(10000l);
        env.getCheckpointConfig().setCheckpointTimeout(5000L);
        env.setParallelism(2);
        return env;
    }

    private StreamExecutionEnvironment buildJob(List<List<AbstractDataProcessNode>> flows, Map<String, Object> jobParamters) {
        StreamExecutionEnvironment env = creatStreamExecutionEnvironment();
        flows.forEach(flow -> {
            DataStream preDtaStream = null;
            for (int i = 0; i < flow.size(); i++) {
                AbstractDataProcessNode abstractDataProcessNode = flow.get(i);
                if (abstractDataProcessNode instanceof AbstractFlinkNode) {
                    AbstractFlinkNode abstractFlinkNode = (AbstractFlinkNode) abstractDataProcessNode;
                    preDtaStream = abstractFlinkNode.handle(env, preDtaStream);
                }
            }
        });
        return env;
    }

    @Override
    public void run(List<List<AbstractDataProcessNode>> flows, Map<String, Object> jobParamters) {
        StreamExecutionEnvironment env = buildJob(flows, jobParamters);
        try {
            env.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
