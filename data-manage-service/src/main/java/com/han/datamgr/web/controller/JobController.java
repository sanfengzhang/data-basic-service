package com.han.datamgr.web.controller;

import com.han.datamgr.exception.BusException;
import com.han.datamgr.web.CommonResponse;
import org.springframework.web.bind.annotation.*;

/**
 * @author: Hanl
 * @date :2019/10/24
 * @desc:
 */
@RestController
@RequestMapping("/api/v1/job")
public class JobController {

    /**
     * 运行ETL_JOB，可以下发该Job下关联的数据流程配置，指定需要下发的流程
     * 不指定的时候，就根据数据库配置的job-flow关系进行下发
     * @param id
     * @param flowIds
     * @return
     * @throws BusException
     */
    @GetMapping("/{id}")
    public CommonResponse runJob(@PathVariable String id, @RequestParam String flowIds) throws BusException {

        return CommonResponse.buildWithSuccess();
    }


}
