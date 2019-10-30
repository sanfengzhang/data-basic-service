package com.han.datamgr.web.controller;

import com.alibaba.fastjson.JSON;
import com.han.datamgr.core.CommandPipeLineService;
import com.han.datamgr.exception.BusException;
import com.han.datamgr.web.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: Hanl
 * @date :2019/10/24
 * @desc:
 */
@RestController
@RequestMapping("/api/v1/job")
public class JobController {

    @Autowired
    private CommandPipeLineService commandPipeLineService;

    /**
     * 运行ETL_JOB，可以下发该Job下关联的数据流程配置，指定需要下发的流程
     * 不指定的时候，就根据数据库配置的job-flow关系进行下发
     *
     * @param id
     * @param flowIds
     * @return
     * @throws BusException
     */
    @GetMapping("/{id}")
    public CommonResponse runJob(@PathVariable String id, @RequestParam String flowIds) throws BusException {

        return CommonResponse.buildWithSuccess();
    }


    @GetMapping
    public String getJobConfig() throws BusException {
        Map<String, Object> jobParams = new HashMap<>();
        jobParams.put("restartAttempts", 3);
        jobParams.put("delayBetweenAttempts", 30000);
        jobParams.put("timeCharacteristic", "EventTime");
        jobParams.put("checkpointInterval", 15000);
        jobParams.put("checkpointTimeout", 30000);
        jobParams.put("parallelism", 2);
        jobParams.put("flink.source.socket.host", "127.0.0.1");
        jobParams.put("flink.source.socket.port", "8085");
        jobParams.put("flink.source.socket.data_type", "test_socket_type");
        Map<String, Object> cmdMap = commandPipeLineService.buildCommandPipeline("8adb929b6dcf4089016dcf40b16c0000").get();
        jobParams.put("flink.etl.morph_flow", cmdMap);
        return JSON.toJSONString(jobParams);
    }
}
