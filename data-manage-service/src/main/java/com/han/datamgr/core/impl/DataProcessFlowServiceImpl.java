package com.han.datamgr.core.impl;

import com.han.datamgr.core.DataProcessFlowService;
import com.han.datamgr.entity.DataProcessFlowEntity;
import com.han.datamgr.exception.BusException;
import com.han.datamgr.repository.DataProcessFlowRepository;
import com.han.datamgr.vo.DataProcessFlowVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Hanl
 * @date :2019/9/29
 * @desc:
 */
@Service
public class DataProcessFlowServiceImpl implements DataProcessFlowService {

    @Autowired
    private DataProcessFlowRepository flowRepository;

    @Override
    public List<DataProcessFlowVO> queryDataProcessFlows(String id) throws BusException {
        List<DataProcessFlowVO> result = new ArrayList<>();
        List<DataProcessFlowEntity> data = null;
        if (StringUtils.isEmpty(id)) {
            data = flowRepository.findAll();
        } else {
            DataProcessFlowEntity entity = flowRepository.findById(id).get();
            data = new ArrayList<>();
            data.add(entity);
        }

        if (null != data) {
            for (DataProcessFlowEntity entity : data) {
                DataProcessFlowVO vo = new DataProcessFlowVO();
                vo.setFlowEntity(entity);
                vo.formEntityToLineList();
                vo.fromEntityToNodeList();
                result.add(vo);
            }
        }
        return result;
    }


    @Override
    public void createDataProcessFlow(DataProcessFlowVO vo) throws BusException {
        DataProcessFlowEntity entity = new DataProcessFlowEntity();
        entity.setDataProcessFlowName(vo.getDataProcessFlowName());
        entity.setLoadExternalLibsPath(vo.getLoadExternalLibsPath());
        flowRepository.save(entity);
    }

    @Override
    public void updateDataProcessFlowCommandDetail(DataProcessFlowVO dataProcessFlowVO) throws BusException {

    }

    @Override
    public void removeDataProcessFlow(DataProcessFlowVO dataProcessFlowVO) throws BusException {

    }


}
