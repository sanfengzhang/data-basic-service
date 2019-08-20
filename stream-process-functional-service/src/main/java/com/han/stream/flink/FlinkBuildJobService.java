package com.han.stream.flink;

import com.han.dataflow.api.model.AbstractDataProcessNode;
import com.han.stream.BuildJobService;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.streaming.api.TimeCharacteristic;
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

    @Override
    public void buildJobFromDataProcessFlow(List<List<AbstractDataProcessNode>> flows, Map<String, Object> jobParamters) {
        StreamExecutionEnvironment env = creatStreamExecutionEnvironment();


    }
}
