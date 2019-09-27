package com.han.datamgr.support;

import javax.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: Hanl
 * @date :2019/9/27
 * @desc:
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AlreadyInDBConstraintValidator.class)
public @interface AlreadyInDB {

    String sql();

}
