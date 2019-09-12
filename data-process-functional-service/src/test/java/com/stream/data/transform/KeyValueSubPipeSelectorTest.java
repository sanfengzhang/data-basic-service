package com.stream.data.transform;

import org.junit.Test;
import org.kitesdk.morphline.api.Command;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author: Hanl
 * @date :2019/9/12
 * @desc:
 */
public class KeyValueSubPipeSelectorTest {

    @Test
    public void testKeyValueSubPipeSelector() throws Exception {
        Class clzz = KeyValueSubPipeSelector.class;

        //KeyValueSubPipeSelector<String> keyValueSubPipeSelector = (KeyValueSubPipeSelector) clzz.newInstance();
        Set<Integer> ageSet = new HashSet<>();
        Map<String, Command> commandMap = null;
        KeyValueSubPipeSelector keyValueSubPipeSelector = (KeyValueSubPipeSelector) clzz.getConstructor(new Class[]{String.class, Set.class, Map.class}).newInstance("name", ageSet, commandMap);
        System.out.println(keyValueSubPipeSelector.select(null));
    }

}
