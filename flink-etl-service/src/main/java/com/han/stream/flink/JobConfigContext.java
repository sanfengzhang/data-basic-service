package com.han.stream.flink;

import com.stream.data.transform.model.CommandPipeline;

import java.util.Map;

/**
 * @author: Hanl
 * @date :2019/8/29
 * @desc:
 */
public interface JobConfigContext {

    public Map<String, Object> getJobConfigParams();

    public Long getLong(String key) throws Exception;

    public String getString(String key) throws Exception;

    public int getInt(String key) throws Exception;

    public Map<String, CommandPipeline> getCommandPipelineMap(String key) throws Exception;

}
