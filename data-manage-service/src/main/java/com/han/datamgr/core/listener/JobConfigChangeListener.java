package com.han.datamgr.core.listener;

import com.han.datamgr.common.EventListener;
import com.han.datamgr.exception.EventException;
import com.han.datamgr.vo.EventVO;
import org.springframework.stereotype.Service;

/**
 * @author: Hanl
 * @date :2019/9/29
 * @desc:
 * 数据处理流程变化监听器, 就是在操作数据流程相关的操作的时候，是否需要
 * 向大数据平台处理发送相关的变化
 * 1.平台的基本参数配置变化，比如checkPoint时间、并行度
 * 2.ETL处理过程中的变化，比如EL表达式变化等、是否禁用某个命令
 * 3.任务操作等都可以从该web平台发送出去
 * FIXME 找个Listener的触发时机应该是在所有服务操作事物commit之后再下发，
 *    不要在事物范围内执行
 */
@Service
public class JobConfigChangeListener implements EventListener {

    @Override
    public EventVO listen(EventVO event) throws EventException {

        return null;
    }
}
