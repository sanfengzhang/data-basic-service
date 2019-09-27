package com.han.datamgr.support;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author: Hanl
 * @date :2019/9/27
 * @desc:
 */
public class AlreadyInDBConstraintValidator implements ConstraintValidator<AlreadyInDB, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        return false;
    }

    @Override
    public void initialize(AlreadyInDB constraintAnnotation) {

    }
}
