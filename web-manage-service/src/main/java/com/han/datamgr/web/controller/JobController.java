package com.han.datamgr.web.controller;

import com.alibaba.fastjson.JSON;
import com.han.datamgr.core.CommandPipeLineService;
import com.han.datamgr.core.JobService;
import com.han.datamgr.exception.BusException;
import com.han.datamgr.web.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    private JobService jobService;

    @GetMapping("/{id}")
    public CommonResponse getJobConfig(@PathVariable String id) throws BusException {
        Map<String, Object> jobParams = jobService.getJobConfig(id);
        return CommonResponse.buildWithSuccess(jobParams);
    }
}
