package com.han.datamgr.core.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.han.datamgr.core.CommandPipeLineService;
import com.han.datamgr.core.JobService;
import com.han.datamgr.entity.DataProcessFlowEntity;
import com.han.datamgr.entity.JobDataProcessFlowRelationEntity;
import com.han.datamgr.entity.JobEntity;
import com.han.datamgr.exception.BusException;
import com.han.datamgr.repository.JobRepository;
import com.han.datamgr.vo.JobVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author: Hanl
 * @date :2019/11/6
 * @desc:
 */
@Service
public class JobServiceImpl implements JobService {

    @Autowired
    private CommandPipeLineService commandPipeLineService;

    @Autowired
    private JobRepository jobRepository;

    @Override
    public Map<String, Object> getJobConfig(String jobId) throws BusException {
        Map<String, Object> jobParams = new HashMap<>();
        Optional<JobEntity> optionalJobEntity = jobRepository.findById(jobId);
        if (!optionalJobEntity.isPresent()) {
            throw new BusException("没有找到对应的job,jobId=" + jobId);
        }
        JobEntity jobEntity = optionalJobEntity.get();

        String configString = jobEntity.getJobConfigParams();
        Map<String, Object> jobConfig = JSON.parseObject(configString, new TypeReference<Map<String, Object>>() {
        });
        jobParams.putAll(jobConfig);
        List<JobDataProcessFlowRelationEntity> relSet = jobEntity.getJobFlowRelSet();
        if (null == relSet || relSet.size() == 0) {
            throw new BusException("Job下面没有配置数据处理流程");
        }
        //FIXME 这里先只支持一个Job对应一条数据处理流程
        DataProcessFlowEntity flowEntity = relSet.get(0).getDataProcessFlowEntity();
        List<Map<String, Object>> list = commandPipeLineService.buildCommandMapConfig(flowEntity.getId());
        jobParams.put("flink.etl.morph_flow", list);
        return jobParams;
    }


    @Override
    public void crateJob(JobVO jobVO) throws BusException {

    }

    @Override
    public void updateJob(JobVO jobVO) throws BusException {

    }

    @Override
    public void removeJob(JobVO jobVO) throws BusException {

    }

    @Override
    public void queryJobs() throws BusException {

    }


}
