package com.han.datamgr.core;

import com.han.datamgr.common.EventListener;
import com.han.datamgr.vo.DataProcessFlowVO;
import org.springframework.stereotype.Service;

/**
 * @author: Hanl
 * @date :2019/9/27
 * @desc: 数据处理流程服务
 */
@Service
public interface DataProcessFlowService {

    /**
     * 创建数据流程
     *
     * @param dataProcessFlowVO
     */
    public void createDataProcessFlow(DataProcessFlowVO dataProcessFlowVO);

    /**
     * 修改数据流程命令详细配
     *
     * @param dataProcessFlowVO
     */
    public void updateDataProcessFlowCommandDetail(DataProcessFlowVO dataProcessFlowVO);


    /**
     * 移除数据流程
     *
     * @param dataProcessFlowVO
     */
    public void removeDataProcessFlow(DataProcessFlowVO dataProcessFlowVO);

}
