package com.han.stream.flink.ETL;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.han.stream.flink.BaseFlinkJob;
import com.han.stream.flink.function.DefaultTransformFunction;
import com.han.stream.flink.connectors.DefaultElasticsearchSink;
import com.han.stream.flink.connectors.KafkaSource;
import com.han.stream.flink.connectors.SocketSource;
import com.han.stream.flink.support.Constants;
import com.han.stream.flink.support.Message;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.OutputTag;

import java.util.List;
import java.util.Map;

/**
 * @author: Hanl
 * @date :2019/10/29
 * @desc:
 */
@Slf4j
public class ETLFlinkJob extends BaseFlinkJob {

    public ETLFlinkJob() {
        log.info("*************Start ETL Job*******");
        this.initJobConfigContextByHttp("http://127.0.0.1:8080/ETL/api/v1/job/8adb929b6dcf4089016dcf40b1b70003");
    }

    @Override
    public void run() throws Exception {

        List<Map<String, Object>> morphFlows = JSON.parseObject(jobConfigContext.getString("flink.etl.morph_flow"), new TypeReference<List<Map<String, Object>>>() {
        });
        StreamExecutionEnvironment env = creatStreamExecutionEnvironment();
        Map<String, Object> source = jobConfigContext.getMap("flink.etl.job.source");
        DataStream<Message> dataStreamSourceMessage = null;
        if (source.containsKey("socketSource")) {
            dataStreamSourceMessage = new SocketSource(jobConfigContext).getSource(env);
        } else if (source.containsKey("kafkaSource")) {
            dataStreamSourceMessage = new KafkaSource(jobConfigContext).getSource(env);
        }
        SingleOutputStreamOperator<Map<String, Object>> mapDataStream = dataStreamSourceMessage.process(new DefaultTransformFunction("Flink_Transform_Context", jobConfigContext.getString("flink.etl.main_flow_name"), morphFlows));
        mapDataStream.getSideOutput(new OutputTag<Map<String, Object>>(Constants.FLINK_FAILED) {
        }).print();


        mapDataStream.addSink(new DefaultElasticsearchSink(jobConfigContext).elasticsearchSink());
        env.execute();
    }

    public static void main(String[] args) throws Exception {
        ETLFlinkJob job = new ETLFlinkJob();
        job.run();
    }
}
