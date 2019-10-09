package com.han.datamgr.support;

import com.han.datamgr.common.CommonRepositoryService;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Map;

/**
 * @author: Hanl
 * @date :2019/9/27
 * @desc:
 */
@Configurable(autowire = Autowire.BY_TYPE, dependencyCheck = true)
public class AlreadyInDBConstraintValidator implements ConstraintValidator<AlreadyInDB, String> {

    String tableName;

    String columnName;

    @Autowired
    private CommonRepositoryService commonRepositoryService;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        return commonRepositoryService.existInDB(tableName, columnName, new Object[]{value});
    }

    @Override
    public void initialize(AlreadyInDB constraintAnnotation) {
        tableName = constraintAnnotation.table();
        columnName = constraintAnnotation.where();
    }
}
