package com.han.stream.flink;

import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.typeinfo.BasicTypeInfo;
import org.apache.flink.streaming.api.datastream.BroadcastStream;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.BroadcastProcessFunction;
import org.apache.flink.util.Collector;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: Hanl
 * @date :2019/8/29
 * @desc:
 */
public class StreamJobTest {

    public static void main(String[] args) throws Exception {
        // set up the streaming execution environment
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        BroadcastStream<Integer> configEventStream = env.socketTextStream("127.0.0.1", 8085).map(new RichMapFunction<String, Integer>() {
            @Override
            public Integer map(String value) throws Exception {
                return Integer.valueOf(value);
            }
        }).broadcast(new MapStateDescriptor<>("config_descriptor", BasicTypeInfo.STRING_TYPE_INFO, BasicTypeInfo.BIG_INT_TYPE_INFO));


        DataStream<Map<String, String>> dataStream = env.socketTextStream("127.0.0.1", 8086).map(new RichMapFunction<String, Map<String, String>>() {
            @Override
            public Map<String, String> map(String value) throws Exception {
                String[] arr = value.split("\\|");
                Map<String, String> result = new HashMap<>();
                result.put("name", arr[0]);
                result.put("age", arr[1]);
                return result;
            }
        });
        dataStream.connect(configEventStream).process(new BroadcastProcessFunction<Map<String, String>, Integer, Map<String, String>>() {

            private int broadcast = 0;

            @Override
            public void processElement(Map<String, String> value, ReadOnlyContext ctx, Collector<Map<String, String>> out) throws Exception {
                System.out.println("Thread---"+Thread.currentThread().getId());
                value.put("broadcast", broadcast + "");
                out.collect(value);
            }

            @Override
            public void processBroadcastElement(Integer value, Context ctx, Collector<Map<String, String>> out) throws Exception {
                System.out.println("Thread---"+Thread.currentThread().getId());
                broadcast = value;
                System.out.println("实例变量broadcast更新成功：" + value);
                Map<String, String> change = new HashMap<>();
                change.put("broadcast", broadcast + "");
                out.collect(change);

            }
        }).map(new RichMapFunction<Map<String, String>, String>() {

            @Override
            public String map(Map<String, String> value) throws Exception {

                return value.toString();
            }
        }).print();

        env.execute("Flink Streaming Java API Skeleton");
    }
}
