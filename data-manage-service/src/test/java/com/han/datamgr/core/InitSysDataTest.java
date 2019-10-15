package com.han.datamgr.core;

import com.alibaba.fastjson.JSON;
import com.han.datamgr.Application;
import com.han.datamgr.entity.*;
import com.han.datamgr.exception.BusException;
import com.han.datamgr.repository.*;
import com.han.datamgr.vo.CommandInstanceVO;
import com.han.datamgr.vo.CommandVO;
import com.stream.data.transform.api.CommandBuildService;
import net.bytebuddy.asm.Advice;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.collections.ListUtil;
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

    @Autowired
    private FiledRepository filedRepository;

    @Autowired
    private CommandInstanceRepository commandInstanceRepository;

    @Autowired
    private ParamRepository pa;

    @Autowired
    private CommandParamRepository commandParamRepository;

    @Autowired
    private CommandRepository commandRepository;

    @Transactional(rollbackFor = {Exception.class})
    @Test
    @Rollback(false)
    public void createCmdParamTest() {

        CommandParamEntity commandParamEntity = new CommandParamEntity();
        commandParamEntity.setFieldName("expresses");
        commandParamEntity.setFieldType("java.util.HashMap");
        commandParamEntity.setFiledValue("{\"trans_return_code<0 \"?\" 99999 \":\"trans_return_code\":\"java.lang.Integer,trans_return_code\"}");
        commandParamRepository.save(commandParamEntity);

    }


    @Transactional(rollbackFor = {Exception.class})
    @Test
    @Rollback(false)
    public void queryCmdTest() {

        List<CommandEntity> commandInstanceEntity = commandRepository.findAll();
        System.out.println(commandInstanceEntity.toString());
    }


    @Transactional(rollbackFor = {Exception.class})
    @Test
    @Rollback(false)
    public void createQueryTest() {

        CommandInstanceEntity commandInstanceEntity = commandInstanceRepository.findById("8adb929b6db489f7016db48a173a0000").get();
        System.out.println(commandInstanceEntity.toString());
    }


    @Transactional(rollbackFor = {Exception.class})
    @Test
    @Rollback(false)
    public void createFiledTest() {
        FiledEntity filedEntity = filedRepository.findById("8adb929b6dbe052a016dbe0548270000").get();

        FiledEntity filedEntity1 = new FiledEntity();
        filedEntity1.setFieldName("struct");
        filedEntity1.setFieldType("map");
        List<FiledEntity> list = new ArrayList<>();
        list.add(filedEntity);
        filedRepository.save(filedEntity1);
    }

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
        expressCommand.forEach((k, v) -> {

        });

        //vo.setCommandParams(expressCommand);
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
