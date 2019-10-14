package com.han.datamgr.web.controller;

import com.han.datamgr.core.DataProcessFlowService;
import com.han.datamgr.exception.BusException;
import com.han.datamgr.vo.DataProcessFlowVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author: Hanl
 * @date :2019/9/29
 * @desc:
 */
@RestController
@RequestMapping("/api/v1/dataflow")
public class DataProcessFlowController {

    @Autowired
    private DataProcessFlowService dataProcessFlowService;

    @RequestMapping(method = RequestMethod.GET)
    public List<DataProcessFlowVO> findDataFlowsByCondition(DataProcessFlowVO dataProcessFlowVO) throws BusException {

        return dataProcessFlowService.queryDataProcessFlows(dataProcessFlowVO);
    }

}
