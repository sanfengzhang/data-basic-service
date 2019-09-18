package com.stream.data.transform.command;

import com.alibaba.fastjson.parser.ParserConfig;
import com.stream.data.transform.utils.TypeUtils;
import com.typesafe.config.Config;
import org.kitesdk.morphline.api.*;
import org.kitesdk.morphline.base.AbstractCommand;
import org.kitesdk.morphline.base.Configs;

import java.util.*;

/**
 * @author: Hanl
 * @date :2019/8/8
 * @desc:
 */
public class RecordFieldTypeConvertBuilder implements CommandBuilder {

    @Override
    public Collection<String> getNames() {

        return Collections.singletonList("recordFieldType");
    }

    @Override
    public Command build(Config config, Command parent, Command child, MorphlineContext context) {
        try {
            return new RecordFieldTypeConvert(this, config, parent, child, context);
        } catch (Exception e) {
            throw new MorphlineCompilationException("RecordFieldType init failed", config, e);
        }
    }

    private static final class RecordFieldTypeConvert extends AbstractCommand {

        private Map<String, String[]> fieldTypeMap = new HashMap<>();

        private ParserConfig parserConfig = null;

        public RecordFieldTypeConvert(CommandBuilder builder, Config config, Command parent, Command child, MorphlineContext context) {
            super(builder, config, parent, child, context);
            Config fieldTypeConfig = getConfigs().getConfig(config, "fieldTypeMap");
            for (Map.Entry<String, Object> entry : new Configs().getEntrySet(fieldTypeConfig)) {
                String valueArr[] = entry.getValue().toString().split(",");
                fieldTypeMap.put(entry.getKey(), valueArr);
            }
            this.parserConfig = (ParserConfig) context.getSettings().get("parserConfig");
            if (null == parserConfig) {
                throw new MorphlineCompilationException("RecordFieldType init failed because of parserConfig is null", config);
            }
            validateArguments();
        }

        @Override
        protected boolean doProcess(Record record) {

            fieldTypeMap.forEach((k, v) -> {
                Object target = TypeUtils.fastJsonCast(record.getFirstValue(k), v[0], parserConfig);
                record.replaceValues(k, target);
            });
            return super.doProcess(record);
        }
    }
}
