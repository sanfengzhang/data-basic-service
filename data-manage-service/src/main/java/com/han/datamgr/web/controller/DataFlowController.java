package com.han.datamgr.web.controller;

import com.alibaba.fastjson.JSON;
import com.han.datamgr.core.DataProcessFlowService;
import com.han.datamgr.exception.BusException;
import com.han.datamgr.vo.DataProcessFlowVO;
import com.han.datamgr.web.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author: Hanl
 * @date :2019/10/17
 * @desc:
 */
@RestController
@RequestMapping("/api/v1/flow")
public class DataFlowController {

    @Autowired
    private DataProcessFlowService flowService;

    @RequestMapping(method = RequestMethod.GET)
    public CommonResponse findDataFlow() throws BusException {

        return CommonResponse.buildWithSuccess( flowService.queryDataProcessFlows(null));
    }

    @RequestMapping(method = RequestMethod.POST)
    public String createDataFlow(@RequestBody @Valid DataProcessFlowVO dataProcessFlowVO) throws BusException {

        return JSON.toJSONString(CommonResponse.buildWithSuccess("success"));
    }
}
