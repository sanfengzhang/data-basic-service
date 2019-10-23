package com.han.datamgr.core;

import com.han.datamgr.Application;
import com.han.datamgr.entity.*;
import com.han.datamgr.exception.BusException;
import com.han.datamgr.repository.*;
import com.han.datamgr.vo.CommandInstanceVO;
import com.han.datamgr.vo.CommandVO;
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

    @Autowired
    private FlowLineRepository flowLineRepository;


    public void creatWithRollbackTest() throws Exception {
        CommandInstanceEntity commandInstanceEntity = new CommandInstanceEntity();
        commandInstanceEntity.setCommandInstanceName("SOC-逗号分隔符解析-,");
        List<CommandEntity> list = commandRepository.findAll();
        Optional<CommandEntity> commandEntity = commandRepository.findById("8adb929b6dcf3eb1016dcf3edb0e0000");
        if (!commandEntity.isPresent()) {
            System.out.println("error");
        }
        commandInstanceEntity.setCommand(commandEntity.get());
        commandInstanceRepository.save(commandInstanceEntity);
    }


    @Test
    public void createFlowLineTest() {
        FlowLineEntity flowLine = new FlowLineEntity();
        flowLine.setStart(commandInstanceRepository.findById("8adb929b6dd3012b016dd30148800009").get());
        flowLine.setEnd(commandInstanceRepository.findById("8adb929b6dec0ef0016dec0f08e30001").get());
        flowLine.setFlowEntity(dataProcessFlowRepository.findById("8adb929b6dcf4089016dcf40b16c0000").get());
        flowLineRepository.save(flowLine);
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
        outputFields.setFieldType("java.util.List");
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

        CommandParamEntity importCommands = new CommandParamEntity();
        importCommands.setFieldName("importCommands");
        importCommands.setFieldType("java.util.List");
        importCommands.setFiledValue("[\"org.kitesdk.**\",\"com.stream.data.transform.command.*\"]");
        importCommands.setCommandInstanceEntity(commandInstanceEntity);

        Set<CommandParamEntity> commandParamEntityList = new HashSet<>();
        commandParamEntityList.add(inputField);
        commandParamEntityList.add(outputFields);
        commandParamEntityList.add(separator);
        commandParamEntityList.add(isRegex);
        commandParamEntityList.add(addEmptyStrings);
        commandParamEntityList.add(trim);
        commandParamEntityList.add(limit);
        commandParamEntityList.add(importCommands);
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

        CommandParamEntity importCommands1 = new CommandParamEntity();
        importCommands1.setFieldName("importCommands");
        importCommands1.setFieldType("java.util.List");
        importCommands1.setFiledValue("[\"org.kitesdk.**\",\"com.stream.data.transform.command.*\"]");
        importCommands1.setCommandInstanceEntity(commandInstanceEntity1);


        Set<CommandParamEntity> commandParamEntityList1 = new HashSet<>();
        commandParamEntityList1.add(cache_warming);
        commandParamEntityList1.add(expresses);
        commandParamEntityList1.add(importCommands1);
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

    @Test
    public void createSubFlow() {

        CommandEntity entity = new CommandEntity();
        entity.setCommandName("子流程节点");
        entity.setCommandClazz("com.stream.data.transform.command.CallSubPipeBuilder");
        entity.setCommandType("富化");
        entity.setCommandMorphName("callSubPipe");
        CommandInstanceEntity instanceEntity = new CommandInstanceEntity();
        instanceEntity.setCommand(entity);
        instanceEntity.setCommandInstanceName("子流程节点");
        instanceEntity.setTop("223px");
        instanceEntity.setLeft("851px");
        instanceEntity.setIco("el-icon-goods");
        instanceEntity.setShow(true);
        entity.getCommandInstanceEntityList().add(instanceEntity);

        CommandParamEntity paramEntity = new CommandParamEntity();
        paramEntity.setCommandInstanceEntity(instanceEntity);
        paramEntity.setFieldName("pipelineSelectorKey");
        paramEntity.setFieldType("java.lang.String");
        paramEntity.setFiledValue("keyValueSubPipeSelector");
        paramEntity.setCmdDisplayName("选择器名称");

        CommandParamEntity paramEntity1 = new CommandParamEntity();
        paramEntity1.setCommandInstanceEntity(instanceEntity);
        paramEntity1.setFieldName("continueParentPipe");
        paramEntity1.setFieldType("java.lang.Boolean");
        paramEntity1.setFiledValue("true");
        paramEntity1.setCmdDisplayName("是否继续父流程");


        CommandParamEntity importCommands1 = new CommandParamEntity();
        importCommands1.setFieldName("importCommands");
        importCommands1.setFieldType("java.util.List");
        importCommands1.setFiledValue("[\"org.kitesdk.**\",\"com.stream.data.transform.command.*\"]");

        instanceEntity.getCmdInstanceParams().add(paramEntity);
        instanceEntity.getCmdInstanceParams().add(paramEntity1);
        instanceEntity.getCmdInstanceParams().add(importCommands1);
        commandRepository.save(entity);
    }


    @Transactional(rollbackFor = Exception.class)
    @Test
    public void createCmdFuhuaTest() {
        CommandEntity entity = new CommandEntity();
        entity.setCommandName("JAVA类-富化");
        entity.setCommandClazz("com.stream.data.transform.command.JavaMethodAddValueBuilder");
        entity.setCommandType("富化");
        entity.setCommandMorphName("javaMethodAddValue");
        CommandInstanceEntity instanceEntity = new CommandInstanceEntity();
        instanceEntity.setCommand(entity);
        instanceEntity.setCommandInstanceName("IP富化");
        instanceEntity.setTop("223px");
        instanceEntity.setLeft("451px");
        instanceEntity.setIco("el-icon-goods");
        instanceEntity.setShow(true);
        entity.getCommandInstanceEntityList().add(instanceEntity);

        CommandParamEntity paramEntity = new CommandParamEntity();
        paramEntity.setCommandInstanceEntity(instanceEntity);
        paramEntity.setFieldName("class_name");
        paramEntity.setFieldType("java.lang.String");
        paramEntity.setFiledValue("com.stream.data.transform.utils.IpaddressUtil");
        paramEntity.setCmdDisplayName("类名称");

        CommandParamEntity paramEntity1 = new CommandParamEntity();
        paramEntity1.setCommandInstanceEntity(instanceEntity);
        paramEntity1.setFieldName("method_name");
        paramEntity1.setFieldType("java.lang.String");
        paramEntity1.setFiledValue("getIplongValue");
        paramEntity1.setCmdDisplayName("方法名称");



        CommandParamEntity paramEntity3 = new CommandParamEntity();
        paramEntity3.setCommandInstanceEntity(instanceEntity);
        paramEntity3.setFieldName("original_key");
        paramEntity3.setFieldType("java.lang.String");
        paramEntity3.setFiledValue("ip");
        paramEntity3.setCmdDisplayName("富化字段名称");

        CommandParamEntity paramEntity4 = new CommandParamEntity();
        paramEntity4.setCommandInstanceEntity(instanceEntity);
        paramEntity4.setFieldName("derive_key");
        paramEntity4.setFieldType("java.lang.String");
        paramEntity4.setFiledValue("IPValue");
        paramEntity4.setCmdDisplayName("富化后字段名称");

        CommandParamEntity paramEntity5 = new CommandParamEntity();
        paramEntity5.setCommandInstanceEntity(instanceEntity);
        paramEntity5.setFieldName("argument_class");
        paramEntity5.setFieldType("java.lang.String");
        paramEntity5.setFiledValue("java.lang.String");
        paramEntity5.setCmdDisplayName("富化后字段类型");

        CommandParamEntity importCommands1 = new CommandParamEntity();
        importCommands1.setFieldName("importCommands");
        importCommands1.setFieldType("java.util.List");
        importCommands1.setFiledValue("[\"org.kitesdk.**\",\"com.stream.data.transform.command.*\"]");

        instanceEntity.getCmdInstanceParams().add(paramEntity);
        instanceEntity.getCmdInstanceParams().add(paramEntity1);
        instanceEntity.getCmdInstanceParams().add(paramEntity3);
        instanceEntity.getCmdInstanceParams().add(paramEntity4);
        instanceEntity.getCmdInstanceParams().add(paramEntity5);
        instanceEntity.getCmdInstanceParams().add(importCommands1);

        commandRepository.save(entity);
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
        List<JobDataProcessFlowRelationEntity> jobEntities = jobEntity.getJobFlowRelSet();

        JobDataProcessFlowRelationEntity jobDataProcessFlowRelationEntity = new JobDataProcessFlowRelationEntity();
        jobDataProcessFlowRelationEntity.setJobEntity(jobEntity);
        jobDataProcessFlowRelationEntity.setDataProcessFlowEntity(dataProcessFlowEntity);
        jobEntities.add(jobDataProcessFlowRelationEntity);

        jobRepository.saveAndFlush(jobEntity);
    }

}
