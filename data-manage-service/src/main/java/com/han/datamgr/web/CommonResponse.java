package com.han.datamgr.web;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author: Hanl
 * @date :2019/10/9
 * @desc:
 */
@Data
@ToString
public class CommonResponse implements Serializable {

    private int code;

    private Object data;

    public static CommonResponse buildWithSuccess(Object data) {
        CommonResponse commonResponse = new CommonResponse();
        commonResponse.setData(data);
        return commonResponse;
    }

    public static CommonResponse buildWithException(Object data) {
        CommonResponse commonResponse = new CommonResponse();
        commonResponse.setData(data);
        return commonResponse;
    }

}
