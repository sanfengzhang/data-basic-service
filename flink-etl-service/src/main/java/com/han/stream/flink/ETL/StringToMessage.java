package com.han.stream.flink.ETL;

import com.han.stream.flink.JobConfigContext;
import com.han.stream.flink.support.Message;
import org.apache.flink.api.common.functions.MapFunction;

/**
 * @author: Hanl
 * @date :2019/10/30
 * @desc:
 */
public class StringToMessage implements MapFunction<String, Message> {

    private static final long serialVersionUID = 1L;

    private String dataType;

    private JobConfigContext jobConfigContext;


    public StringToMessage(JobConfigContext jobConfigContext)throws Exception {
        this.jobConfigContext = jobConfigContext;
        this.dataType=jobConfigContext.getString("flink.source.socket.data_type");
    }

    @Override
    public Message map(String value) throws Exception {
        Message message = new Message(dataType, value);
        return message;
    }
}
