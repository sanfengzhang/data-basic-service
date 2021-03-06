package com.han.datamgr.web.controller;

import com.alibaba.fastjson.JSON;
import com.han.datamgr.core.CommandInstanceService;
import com.han.datamgr.exception.BusException;
import com.han.datamgr.vo.CommandInstanceVO;
import com.han.datamgr.web.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Hanl
 * @date :2019/10/28
 * @desc:
 */

@RestController
@RequestMapping("/api/v1/command-instance")
public class CommandInstanceController {

    @Autowired
    private CommandInstanceService instanceService;

    @PostMapping
    public CommonResponse createCommandInstance(@RequestBody CommandInstanceVO commandInstanceVO)throws BusException {
        instanceService.createCmdInstance(commandInstanceVO);
        return CommonResponse.buildWithSuccess();
    }
}
