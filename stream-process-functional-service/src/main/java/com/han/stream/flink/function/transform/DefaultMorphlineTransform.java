package com.han.stream.flink.function.transform;

import com.stream.data.transform.model.CommandPipeline;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author: Hanl
 * @date :2019/8/30
 * @desc:
 */
public class DefaultMorphlineTransform extends MorphlineTransform<Map<String, Object>> {

    public DefaultMorphlineTransform(String transformContextName, Map<String, CommandPipeline> commandPipelines) {

        super(transformContextName, commandPipelines);
    }



    @Override
    public Map<String, Object> output(Map<String, Collection<Object>> value) {
        Iterator<Map.Entry<String, Collection<Object>>> it = value.entrySet().iterator();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        while (it.hasNext()) {
            Map.Entry<String, Collection<Object>> en = it.next();

            if (en.getKey().equals("message")) {
                continue;
            }

            resultMap.put(en.getKey(), en.getValue().iterator().next());
        }
        return resultMap;
    }
}
