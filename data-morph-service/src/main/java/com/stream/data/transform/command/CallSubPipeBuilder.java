package com.stream.data.transform.command;

import com.typesafe.config.Config;
import org.kitesdk.morphline.api.*;
import org.kitesdk.morphline.base.AbstractCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

/**
 * @author: Hanl
 * @date :2019/9/12
 * @desc:
 */
public class CallSubPipeBuilder implements CommandBuilder {

    @Override
    public Collection<String> getNames() {
        return Collections.singletonList("callSubPipe");
    }

    @Override
    public Command build(Config config, Command parent, Command child, MorphlineContext context) {
        try {
            return new CallSubPipe(this, config, parent, child, context);
        } catch (Exception e) {
            throw new MorphlineCompilationException("CallSubPipeBuilder", config, new Throwable(e));
        }
    }

    private static final class CallSubPipe extends AbstractCommand {

        private static final Logger logger = LoggerFactory.getLogger(CallSubPipe.class);

        private SubPipeSelector subPipeSelector;

        private String pipelineSelectorKey;

        private boolean continueParentPipe = true;


        public CallSubPipe(CommandBuilder builder, Config config, Command parent, Command child, MorphlineContext context) throws Exception {
            super(builder, config, parent, child, context);
            continueParentPipe = getConfigs().getBoolean(config, "continueParentPipe");
            pipelineSelectorKey=getConfigs().getString(config, "pipelineSelectorKey");
            this.subPipeSelector = (SubPipeSelector) context.getSettings().get(pipelineSelectorKey);
        }

        @Override
        protected boolean doProcess(Record record) {
            Set<Command> commandPipelineSet = subPipeSelector.select(record);
            for (Command cmd : commandPipelineSet) {
                boolean subProcess = cmd.process(record);
                if (!subProcess) {
                    logger.warn("Subprocess execution failed,record={}", record);
                }
            }
            if (!continueParentPipe) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Do not continue with the parent process");
                }

                return true;
            }
            return super.doProcess(record);
        }
    }
}
