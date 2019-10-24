package com.han.datamgr.core.impl;

import com.han.datamgr.core.DataProcessFlowService;
import com.han.datamgr.entity.CommandInstanceEntity;
import com.han.datamgr.entity.DataProcessFlowEntity;
import com.han.datamgr.entity.FlowLineEntity;
import com.han.datamgr.exception.BusException;
import com.han.datamgr.repository.CommandInstanceRepository;
import com.han.datamgr.repository.DataProcessFlowRepository;
import com.han.datamgr.repository.FlowLineRepository;
import com.han.datamgr.utils.FlowUtils;
import com.han.datamgr.vo.FlowVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @author: Hanl
 * @date :2019/9/29
 * @desc:
 */
@Service
@Transactional
public class DataProcessFlowServiceImpl implements DataProcessFlowService {

    @Autowired
    private DataProcessFlowRepository flowRepository;

    @Autowired
    private CommandInstanceRepository commandInstanceRepository;

    @Autowired
    private FlowLineRepository flowLineRepository;

    @Override
    public List<FlowVO> queryDataProcessFlows(String id) throws BusException {
        List<FlowVO> result = new ArrayList<>();
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
                FlowVO vo = new FlowVO();
                vo.setFlowEntity(entity);
                vo.formEntityToLineList();
                vo.fromEntityToNodeList();
                result.add(vo);
            }
        }
        return result;
    }

    /**
     * 保存数据流程     *
     *
     * @param flowVO
     */
    public void saveDataProcessFlow(FlowVO flowVO) throws BusException {
        DataProcessFlowEntity flowEntity = null;
        String id = flowVO.getId();
        if (null == id) {
            DataProcessFlowEntity exist = flowVO.getFlowEntity();
            String existId = exist == null ? null : exist.getId();
            if (null != existId) {
                id = existId;
            }
        }
        if (null == id) {
            flowEntity = new DataProcessFlowEntity();
        } else {
            Optional<DataProcessFlowEntity> optional = flowRepository.findById(id);
            if (!optional.isPresent()) {
                throw new BusException("更新Flow失败,未找到id=" + id);
            }
            flowEntity = optional.get();
        }
        flowEntity.setDataProcessFlowName(flowVO.getDataProcessFlowName());
        flowEntity.setLoadExternalLibsPath(flowVO.getLoadExternalLibsPath());
        flowEntity.setCreateTime(new Date());
        flowRepository.save(flowEntity);
    }

    @Override
    public void saveFlowLineRelation(FlowVO flowVO) throws BusException {
        String id = flowVO.getId();
        if (null == id) {
            DataProcessFlowEntity exist = flowVO.getFlowEntity();
            String existId = exist == null ? null : exist.getId();
            if (null != existId) {
                id = existId;
            }
        }
        Optional<DataProcessFlowEntity> optional = flowRepository.findById(id);
        if (!optional.isPresent()) {
            throw new BusException("更新Flow失败,未找到id=" + id);
        }
        DataProcessFlowEntity flowEntity = optional.get();
        saveFlowLineRelation(flowEntity, flowVO.getLineList());
    }

    private void saveFlowLineRelation(DataProcessFlowEntity flowEntity, List<Map<String, String>> lineList) throws BusException {
        Set<FlowLineEntity> flowLineEntitySet = new HashSet<>();
        if (!CollectionUtils.isEmpty(lineList)) {
            Set<String> cmdInstanceIds = FlowUtils.getCommandInstanceIds(lineList);
            List<CommandInstanceEntity> instanceEntities = commandInstanceRepository.findAllByIdIn(cmdInstanceIds);
            if (cmdInstanceIds.size() != instanceEntities.size()) {
                throw new BusException("有对应的命令实例id未找到数据记录");
            }
            Map<String, CommandInstanceEntity> idInstanceMap = new HashMap<>();
            for (CommandInstanceEntity instanceEntity : instanceEntities) {
                idInstanceMap.put(instanceEntity.getId(), instanceEntity);
            }
            //-----------------------构建命令之间的连线
            for (Map<String, String> en : lineList) {
                FlowLineEntity flowLineEntity = new FlowLineEntity();
                String startId = en.get("from");
                String toId = en.get("to");
                startId = startId.substring(0, startId.indexOf("_"));
                toId = toId.substring(0, toId.indexOf("_"));
                flowLineEntity.setFlowEntity(flowEntity);
               // flowLineEntity.setStart(idInstanceMap.get(startId));
               // flowLineEntity.setEnd(idInstanceMap.get(toId));
                flowLineEntity.setCreateTime(new Date());
                flowLineEntitySet.add(flowLineEntity);
            }
            //------------删除旧的连线
            flowLineRepository.deleteByDataFlowId(flowEntity.getId());
            flowLineRepository.saveAll(flowLineEntitySet);
        }
    }


    public void debugFlow(String id, String data) throws BusException {


    }

}
