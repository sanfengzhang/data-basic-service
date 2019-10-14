package com.han.datamgr.web.controller;

import com.alibaba.fastjson.JSON;
import com.han.datamgr.core.CommandInstanceService;
import com.han.datamgr.exception.BusException;
import com.han.datamgr.vo.LeftMenuVO;
import com.han.datamgr.web.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author: Hanl
 * @date :2019/10/11
 * @desc:
 */
@RestController
@RequestMapping("/api/v1/main")
public class MainPageController {

    @Autowired
    private CommandInstanceService commandInstanceService;

    @RequestMapping(method = RequestMethod.GET)
    public String getLeftMenu() throws BusException {
        List<LeftMenuVO> leftMenuVOList = commandInstanceService.getLeftMenuCmdInstanceData();
        return JSON.toJSONString(CommonResponse.buildWithException(leftMenuVOList)) ;
    }


}
