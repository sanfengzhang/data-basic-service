package com.han.datamgr.web.support;

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

    public static final int SUCCESS_RESP = 200;

    public static final int VALID_FAILED= 300;

    public static final int BUS_FAILED= 400;


    public static CommonResponse buildWithSuccess(Object data) {
        CommonResponse commonResponse = new CommonResponse();
        commonResponse.setCode(SUCCESS_RESP);
        commonResponse.setData(data);
        return commonResponse;
    }

    public static CommonResponse buildWithValidException(Object data) {
        CommonResponse commonResponse = new CommonResponse();
        commonResponse.setCode(VALID_FAILED);
        commonResponse.setData(data);
        return commonResponse;
    }

    public static CommonResponse buildWithBusException(Object data) {
        CommonResponse commonResponse = new CommonResponse();
        commonResponse.setCode(BUS_FAILED);
        commonResponse.setData(data);
        return commonResponse;
    }

}
