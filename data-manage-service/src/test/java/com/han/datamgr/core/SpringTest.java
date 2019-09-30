package com.han.datamgr.core;

import com.han.datamgr.Application;
import com.han.datamgr.entity.DataProcessFlowEntity;
import com.han.datamgr.entity.JobEntity;
import com.han.datamgr.repository.DataProcessFlowRepository;
import com.han.datamgr.repository.JobRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author: Hanl
 * @date :2019/9/30
 * @desc:
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class SpringTest {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private DataProcessFlowRepository dataProcessFlowRepository;

    @Autowired
    private Myservice myservice;


    @Test
    public void testAddData() {

        myservice.testAddData();
    }
}
