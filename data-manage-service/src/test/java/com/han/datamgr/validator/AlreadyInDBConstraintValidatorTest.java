package com.han.datamgr.validator;

import com.han.datamgr.Application;
import com.han.datamgr.vo.BusDataTypeVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author: Hanl
 * @date :2019/9/27
 * @desc:
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class AlreadyInDBConstraintValidatorTest {

    @Test
    public void testAlreadyInDBConstraintValidator() {
        BusDataTypeVO busDataTypeVO = new BusDataTypeVO();
        System.out.println(busDataTypeVO);

    }
}
