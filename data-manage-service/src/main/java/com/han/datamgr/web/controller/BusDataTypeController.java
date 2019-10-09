package com.han.datamgr.web.controller;

import com.han.datamgr.exception.BusException;
import com.han.datamgr.vo.BusDataTypeVO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author: Hanl
 * @date :2019/10/8
 * @desc:
 */
@RestController
@RequestMapping("/api/v1/data_type")
public class BusDataTypeController {

    @RequestMapping(method = RequestMethod.POST)
    public String createDataTypeRequest(@Valid BusDataTypeVO busDataTypeVO) {
        System.out.println(busDataTypeVO);
        return "success";
    }

    @RequestMapping(method = RequestMethod.GET)
    public String queryDataTypeRequest(@RequestParam String name)throws BusException {
        if(name.equals("zhangsan")){
            throw  new BusException("参数错误");
        }
        return "success";
    }

}
