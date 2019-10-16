package com.han.datamgr.core;

import com.han.datamgr.Application;
import com.han.datamgr.entity.*;
import com.han.datamgr.exception.BusException;
import com.han.datamgr.repository.CommandInstanceRepository;
import com.han.datamgr.repository.CommandRepository;
import com.han.datamgr.repository.DataProcessFlowRepository;
import com.han.datamgr.repository.JobRepository;
import com.han.datamgr.vo.CommandVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author: Hanl
 * @date :2019/9/30
 * @desc:
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Transactional
@Rollback(false)
public class InitSysDataTest {

    @Autowired
    private CommandService commandService;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private DataProcessFlowRepository dataProcessFlowRepository;

    @Autowired
    private CommandInstanceRepository commandInstanceRepository;

    @Autowired
    private CommandRepository commandRepository;



    public void creatWithRollbackTest()throws Exception{
        CommandInstanceEntity commandInstanceEntity = new CommandInstanceEntity();
        commandInstanceEntity.setCommandInstanceName("SOC-逗号分隔符解析-,");
        List<CommandEntity> list=commandRepository.findAll();
        Optional<CommandEntity> commandEntity=commandRepository.findById("8adb929b6dcf3eb1016dcf3edb0e0000");
        if(!commandEntity.isPresent()){
            System.out.println("error");
        }
        commandInstanceEntity.setCommand(commandEntity.get());
        commandInstanceRepository.save(commandInstanceEntity);
    }


    @Test
    public void createCmdInstanceTest() {
        CommandInstanceEntity commandInstanceEntity = new CommandInstanceEntity();
        commandInstanceEntity.setCommandInstanceName("SOC-分隔符解析-|");
        commandInstanceEntity.setCommand(commandRepository.findById("8adb929b6dcf3eb1016dcf3edb0e0000").get());

        CommandParamEntity inputField = new CommandParamEntity();
        inputField.setFieldName("inputField");
        inputField.setFieldType("java.lang.String");
        inputField.setFiledValue("message");
        inputField.setCommandInstanceEntity(commandInstanceEntity);

        CommandParamEntity outputFields = new CommandParamEntity();
        outputFields.setFieldName("outputFields");
        outputFields.setFieldType("java.utils.List");
        outputFields.setFiledValue("[\"trans_date\",\"trans_code\",\"trans_channel_id\",\"trans_start_datetime\",\"trans_end_datetime\"," +
                "\"trans_cust_time\",\"trans_org_id\",\"trans_clerk\",\"trans_return_code\",\"trans_err_msg\",\"trans_tuexdo_name\"]");
        outputFields.setCommandInstanceEntity(commandInstanceEntity);

        CommandParamEntity separator = new CommandParamEntity();
        separator.setFieldName("separator");
        separator.setFieldType("java.lang.String");
        separator.setFiledValue("|");
        separator.setCommandInstanceEntity(commandInstanceEntity);

        CommandParamEntity isRegex = new CommandParamEntity();
        isRegex.setFieldName("isRegex");
        isRegex.setFieldType("java.lang.Boolean");
        isRegex.setFiledValue("false");
        isRegex.setCommandInstanceEntity(commandInstanceEntity);

        CommandParamEntity addEmptyStrings = new CommandParamEntity();
        addEmptyStrings.setFieldName("addEmptyStrings");
        addEmptyStrings.setFieldType("java.lang.Boolean");
        addEmptyStrings.setFiledValue("false");
        addEmptyStrings.setCommandInstanceEntity(commandInstanceEntity);

        CommandParamEntity trim = new CommandParamEntity();
        trim.setFieldName("trim");
        trim.setFieldType("java.lang.Boolean");
        trim.setFiledValue("false");
        trim.setCommandInstanceEntity(commandInstanceEntity);

        CommandParamEntity limit = new CommandParamEntity();
        limit.setFieldName("limit");
        limit.setFieldType("java.lang.Integer");
        limit.setFiledValue("11");
        limit.setCommandInstanceEntity(commandInstanceEntity);

        List<CommandParamEntity> commandParamEntityList = new ArrayList<>();
        commandParamEntityList.add(inputField);
        commandParamEntityList.add(outputFields);
        commandParamEntityList.add(separator);
        commandParamEntityList.add(isRegex);
        commandParamEntityList.add(addEmptyStrings);
        commandParamEntityList.add(trim);
        commandParamEntityList.add(limit);
        commandInstanceEntity.setCmdInstanceParams(commandParamEntityList);


        CommandInstanceEntity commandInstanceEntity1 = new CommandInstanceEntity();
        commandInstanceEntity1.setCommandInstanceName("SOC-EL计算");
        commandInstanceEntity1.setCommand(commandRepository.findById("8adb929b6dcf3eb1016dcf3edb3f0001").get());

        CommandParamEntity cache_warming = new CommandParamEntity();
        cache_warming.setFieldName("cache_warming");
        cache_warming.setFieldType("java.util.Map");
        cache_warming.setFiledValue("{\"trans_return_code\":\"999\"}");
        cache_warming.setCommandInstanceEntity(commandInstanceEntity1);

        CommandParamEntity expresses = new CommandParamEntity();
        expresses.setFieldName("expresses");
        expresses.setFieldType("java.util.Map");
        expresses.setFiledValue("{\"trans_return_code<0 \"?\" 99999 \":\"trans_return_code\":\"java.lang.Integer,trans_return_code\"}");
        expresses.setCommandInstanceEntity(commandInstanceEntity1);
        List<CommandParamEntity> commandParamEntityList1 = new ArrayList<>();
        commandParamEntityList1.add(cache_warming);
        commandParamEntityList1.add(expresses);
        commandInstanceEntity1.setCmdInstanceParams(commandParamEntityList1);

        List<CommandInstanceEntity> data = new ArrayList<>();
        data.add(commandInstanceEntity);
        data.add(commandInstanceEntity1);

        commandInstanceRepository.saveAll(data);

    }

    @Transactional(rollbackFor = Exception.class)
    public void createCmdTest() {
        CommandVO commandVO = new CommandVO();
        commandVO.setCommandName("分隔符解析");
        commandVO.setCommandClazz("org.kitesdk.morphline.stdlib.SplitBuilder");
        commandVO.setCommandType("解析");
        commandVO.setMorphName("split");

        CommandVO commandVO1 = new CommandVO();
        commandVO1.setCommandName("EL表达式");
        commandVO1.setCommandClazz("com.stream.data.transform.command.ELExpressBuilder");
        commandVO1.setCommandType("计算");
        commandVO1.setMorphName("EL");

        try {
            commandService.createCommand(new CommandVO[]{commandVO, commandVO1});
        } catch (BusException e) {
            e.printStackTrace();
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void createJobDataFlow() {
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

}
