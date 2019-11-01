package com.stream.data.transform.command;

import org.kitesdk.morphline.api.Command;
import org.kitesdk.morphline.api.MorphlineContext;
import org.kitesdk.morphline.api.Record;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * @author: Hanl
 * @date :2019/11/1
 * @desc:
 */
public class DefaultBranchPipeSelector implements BranchPipeSelector {

    @Override
    public Set<Command> select(Record record, MorphlineContext context) {
        Map<String, Command> allCommand=( Map<String, Command>)context.getSettings().get("allCommand");
        String fieldValue = record.getFirstValue("data_type").toString();
        Command pipe=null;
        if ("SOC_FANGHUOQIANG".equals(fieldValue)) {
             pipe = allCommand.get("数据流程测试1");
        }else if("TRADE_CONF".equals(fieldValue)){

        }

        if (null == pipe) {
            throw new NullPointerException("Field corresponding value[" + fieldValue + "],then pipe is null.");
        }
        return Collections.singleton(pipe);
    }
}
