package com.han.datamgr.core;

import com.han.datamgr.Application;
import com.han.datamgr.entity.JobDataProcessFlowRelationEntity;
import com.han.datamgr.exception.BusException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * @author: Hanl
 * @date :2019/11/6
 * @desc:
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Transactional
@Rollback(false)
public class JobServiceTest {

    @Autowired
    private JobService jobService;

    @Test
    public void testGetJob() throws BusException {
        Map<String, Object> map = jobService.getJobConfig("8adb929b6dcf4089016dcf40b1b70002");
        System.out.println(map);
    }

}
