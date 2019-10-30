package com.han.stream.flink.platform;

import com.han.stream.flink.JobConfigContext;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author: Hanl
 * @date :2019/10/28
 * @desc:
 */
public interface BaseJob {

    public void run() throws Exception;

    public JobConfigContext getJobConfigContext();

}
