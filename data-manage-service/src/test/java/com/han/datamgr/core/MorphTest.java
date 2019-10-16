package com.han.datamgr.core;

import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Hanl
 * @date :2019/10/16
 * @desc:
 */
public class MorphTest {

    @Test
    public void testJson() {
        List<String> outputFields = new ArrayList<>();
        outputFields.add("trans_date");
        outputFields.add("trans_code");
        outputFields.add("trans_channel_id");
        outputFields.add("trans_start_datetime");
        outputFields.add("trans_end_datetime");
        outputFields.add("trans_cust_time");
        outputFields.add("trans_org_id");
        outputFields.add("trans_clerk");
        outputFields.add("trans_return_code");
        outputFields.add("trans_err_msg");
        outputFields.add("trans_tuexdo_name");
        System.out.println(JSON.toJSONString(outputFields));

    }
}
