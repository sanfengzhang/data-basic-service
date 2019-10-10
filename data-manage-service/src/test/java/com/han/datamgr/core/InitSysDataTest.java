package com.han.datamgr.core;

import com.han.datamgr.Application;
import com.han.datamgr.entity.CommandEntity;
import com.han.datamgr.entity.DataProcessFlowEntity;
import com.han.datamgr.entity.JobDataProcessFlowRelationEntity;
import com.han.datamgr.entity.JobEntity;
import com.han.datamgr.exception.BusException;
import com.han.datamgr.repository.CommandRepository;
import com.han.datamgr.repository.DataProcessFlowRepository;
import com.han.datamgr.repository.JobRepository;
import com.han.datamgr.vo.CommandInstanceVO;
import com.han.datamgr.vo.CommandVO;
import com.stream.data.transform.api.CommandBuildService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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

    @Autowired
    private CommandInstanceService commandInstanceService;

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
    @Rollback(false)
    public void createCmdInstanceTest() throws BusException {
        CommandInstanceVO vo = new CommandInstanceVO();
        List<CommandEntity> commandVOS = commandService.queryAllCommands();
        vo.setCommandEntity(commandVOS.get(0));
        vo.setCommandInstanceName("SOC网关日志EL算子");

        Map<String, String> expressMap = new HashMap<>();
        expressMap.put("trans_return_code<0 \"?\" 99999 \":\"trans_return_code", "java.lang.Integer,trans_return_code");
        Map<String, Object> cacheWarmingData = new HashMap<>();
        cacheWarmingData.put("trans_return_code", "999");
        Map<String, Object> expressCommand = CommandBuildService.elExpress(expressMap, cacheWarmingData);
        vo.setCommandParams(expressCommand);
        commandInstanceService.createCmdInstance(vo);
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
