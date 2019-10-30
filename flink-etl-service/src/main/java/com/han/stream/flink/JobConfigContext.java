package com.han.stream.flink;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: Hanl
 * @date :2019/8/29
 * @desc:
 */
public class JobConfigContext {

    private Map<String, Object> jobConfigParams = new HashMap<>();

    public Map<String, Object> getJobConfigParams() {
        return jobConfigParams;
    }

    public Long getLong(String key) throws Exception {
        return Long.parseLong(getString(key));
    }

    public String getString(String key) throws Exception {
        return jobConfigParams.get(key).toString();
    }

    public int getInt(String key) throws Exception {
        return Integer.parseInt(getString(key));
    }
}
