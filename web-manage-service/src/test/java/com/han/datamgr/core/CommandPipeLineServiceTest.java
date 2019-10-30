package com.han.datamgr.core;

import com.han.datamgr.Application;
import com.han.datamgr.exception.BusException;
import com.stream.data.transform.model.CommandPipeline;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author: Hanl
 * @date :2019/10/16
 * @desc:
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Transactional
@Rollback(false)
public class CommandPipeLineServiceTest {

    @Autowired
    private CommandPipeLineService commandPipeLineService;

    @Test
    public void testPipelineTest() throws Exception {
        CommandPipeline commandPipeline = commandPipeLineService.buildCommandPipeline("8adb929b6dcf4089016dcf40b16c0000");
        System.out.println(commandPipeline.get());
        MorphTest morphTest=new MorphTest();
        morphTest.testMorphlin(commandPipeline);
    }

}
