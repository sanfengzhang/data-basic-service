package com.han.datamgr.core;

import com.han.datamgr.Application;
import com.han.datamgr.entity.DataProcessFlowEntity;
import com.han.datamgr.entity.JobDataProcessFlowRelationEntity;
import com.han.datamgr.entity.JobEntity;
import com.han.datamgr.exception.BusException;
import com.han.datamgr.repository.DataProcessFlowRepository;
import com.han.datamgr.repository.JobRepository;
import com.han.datamgr.vo.CommandVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
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
public class InitSysDataTest {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private DataProcessFlowRepository dataProcessFlowRepository;

    @Autowired
    private CommandService commandService;

    @Transactional(rollbackFor = {Exception.class})
    @Test
    @Rollback(false)
    public void createCmdTest() {
        CommandVO commandVO = new CommandVO();
        commandVO.setCommandName("SOC的EL表达式Command");
        commandVO.setCommandClazz("com.stream.data.transform.command.ELExpressBuilder");
        commandVO.setCommandType("计算");
        try {
            commandService.createCommand(commandVO);
        } catch (BusException e) {
            e.printStackTrace();
        }
    }


    @Transactional(rollbackFor = {Exception.class})
    @Test
    public void testAddDataProcessFlowData() {
        DataProcessFlowEntity dataProcessFlowEntity = new DataProcessFlowEntity();
        dataProcessFlowEntity.setDataProcessFlowName("数据处理流程测试1");
        dataProcessFlowEntity.setCreateTime(new Date());
        dataProcessFlowEntity.setVersion(1);

        DataProcessFlowEntity dataProcessFlowEntity1 = new DataProcessFlowEntity();
        dataProcessFlowEntity1.setDataProcessFlowName("数据处理流程测试2");
        dataProcessFlowEntity1.setCreateTime(new Date());
        dataProcessFlowEntity1.setVersion(1);

        List<DataProcessFlowEntity> dataProcessFlowEntityList = new ArrayList<>();
        dataProcessFlowEntityList.add(dataProcessFlowEntity);
        dataProcessFlowEntityList.add(dataProcessFlowEntity1);
        dataProcessFlowRepository.saveAll(dataProcessFlowEntityList);

        JobEntity jobEntity = new JobEntity();
        jobEntity.setJobName("测试Job");
        jobEntity.setCreateTime(new Date());
        List<JobDataProcessFlowRelationEntity> jobEntities = jobEntity.getRelationEntities();

        JobDataProcessFlowRelationEntity jobDataProcessFlowRelationEntity = new JobDataProcessFlowRelationEntity();
        jobDataProcessFlowRelationEntity.setJobEntity(jobEntity);
        jobDataProcessFlowRelationEntity.setDataProcessFlowEntity(dataProcessFlowEntity);
        jobEntities.add(jobDataProcessFlowRelationEntity);

        jobRepository.saveAndFlush(jobEntity);
    }


    @Test
    @Transactional
    public void testFindData() {
        List<JobEntity> list = jobRepository.findAll();
        list.forEach(jobEntity -> {
            System.out.println(jobEntity);
            jobEntity.getRelationEntities().forEach(r -> {
                System.out.println(r.getDataProcessFlowEntity());
            });
        });
    }


}
