package com.han.datamgr.core;

import com.alibaba.fastjson.parser.ParserConfig;
import com.codahale.metrics.SharedMetricRegistries;
import com.stream.data.transform.model.CommandPipeline;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.kitesdk.morphline.api.Command;
import org.kitesdk.morphline.api.MorphlineContext;
import org.kitesdk.morphline.api.Record;
import org.kitesdk.morphline.base.Compiler;
import org.kitesdk.morphline.base.FaultTolerance;
import org.kitesdk.morphline.base.Fields;
import org.kitesdk.morphline.base.Notifications;

import java.util.Map;

/**
 * @author: Hanl
 * @date :2019/10/16
 * @desc:
 */
public class MorphTest {

    private MorphlineContext morphlineContext;

    private Collector finalChid;

    public MorphTest() {
        FaultTolerance faultTolerance = new FaultTolerance(false, false);
        morphlineContext = new MorphlineContext.Builder().setExceptionHandler(faultTolerance)
                .setMetricRegistry(SharedMetricRegistries.getOrCreate("testId")).build();
        this.morphlineContext.getSettings().put("parserConfig", new ParserConfig());
        finalChid = new Collector();

    }

    public void testMorphlin(CommandPipeline commandPipeline) throws Exception {

        Map<String, Object> configMap = commandPipeline.get();
        Config config = ConfigFactory.parseMap(configMap);
        System.out.println(config);
        Command cmd = new Compiler().compile(config, morphlineContext, finalChid);
        Notifications.notifyStartSession(cmd);
        String msg = "1.1.1.1|234|2018-04-17 17:05:08.478679|2018-04-17 17:05:08.483580|0.00|8020800|020777|-100|读取保函注销接口表失败[BHZX201803251590217],记录不存在|1.1.1.1|ccccc";
        Record record = new Record();
        record.put(Fields.MESSAGE, msg);
        boolean flag=cmd.process(record);
        System.out.println(flag);
        record = finalChid.getRecords().get(0);
        System.out.println(record);
        Notifications.notifyShutdown(cmd);

    }
}
