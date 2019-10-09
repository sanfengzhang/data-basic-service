package com.han.datamgr.web.support;


import com.han.datamgr.exception.BusException;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: Hanl
 * @date :2019/10/9
 * @desc:
 */
@ControllerAdvice
public class WebRequestException {

    @ExceptionHandler(value = BindException.class)
    @ResponseBody
    public CommonResponse handleBindException(BindException ex) {
        List<String> defaultMsg = ex.getBindingResult().getAllErrors()
                .stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.toList());
        return CommonResponse.buildWithValidException(defaultMsg);
    }


    /**
     * 单个参数校验
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    @ResponseBody
    public CommonResponse handleBindGetException(ConstraintViolationException ex) {
        List<String> defaultMsg = ex.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());
        return CommonResponse.buildWithValidException(defaultMsg);
    }

    @ExceptionHandler(value = BusException.class)
    @ResponseBody
    public CommonResponse handBusException(BusException ex) {

        return CommonResponse.buildWithBusException(ex.getMessage());
    }

}
