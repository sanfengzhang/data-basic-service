package com.han.datamgr.core.impl;

import com.han.datamgr.core.DataProcessFlowService;
import com.han.datamgr.core.FlowLineService;
import com.han.datamgr.entity.CanvasCommandInstanceEntity;
import com.han.datamgr.entity.CommandInstanceEntity;
import com.han.datamgr.entity.DataProcessFlowEntity;
import com.han.datamgr.entity.FlowLineEntity;
import com.han.datamgr.exception.BusException;
import com.han.datamgr.repository.CanvasCommandInstanceRepository;
import com.han.datamgr.repository.CommandInstanceRepository;
import com.han.datamgr.repository.DataProcessFlowRepository;
import com.han.datamgr.repository.FlowLineRepository;
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
    private CanvasCommandInstanceRepository canvasCommandInstanceRepository;

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
        saveFlowLineRelation(flowEntity, flowVO.getLineList(), flowVO.getNodeList());
    }

    private void saveFlowLineRelation(DataProcessFlowEntity flowEntity, List<Map<String, String>> lineList, List<CanvasCommandInstanceEntity> nodeList) throws BusException {
        if (!CollectionUtils.isEmpty(lineList) || !CollectionUtils.isEmpty(nodeList)) {
            if (CollectionUtils.isEmpty(lineList)) {
                if (nodeList.size() > 1) {
                    throw new BusException("一条流程包含独立的节点只能有一个");
                }
                lineList = new ArrayList<>();
                Map<String, String> line = new HashMap<>();
                line.put(FlowLineService.START_CMD, nodeList.get(0).getId());
                line.put(FlowLineService.END_CMD, null);
                lineList.add(line);
            }
            String flowId = flowEntity.getId();

            Set<String> delCanvasIds = new HashSet<>();
            List<FlowLineEntity> existFlowLines = flowLineRepository.findAllByFlowEntity_Id(flowEntity.getId());
            existFlowLines.forEach(flowLineEntity -> {
                delCanvasIds.add(flowLineEntity.getStart().getId());
                delCanvasIds.add(flowLineEntity.getEnd().getId());
            });
            flowLineRepository.deleteByDataFlowId(flowId);
            canvasCommandInstanceRepository.deleteAllByIdIn(delCanvasIds);

            nodeList.forEach(node -> {
                String id = node.getCommandInstanceEntity().getId();
                CommandInstanceEntity entity = commandInstanceRepository.findById(id).get();
                node.setCommandInstanceEntity(entity);
            });
            nodeList = canvasCommandInstanceRepository.saveAll(nodeList);
            Map<String, CanvasCommandInstanceEntity> idCanvas = new HashMap<>();
            nodeList.forEach(node -> {
                idCanvas.put(node.getId(), node);
            });
            List<FlowLineEntity> data = new ArrayList<>();
            for (Map<String, String> en : lineList) {
                String startId = en.get(FlowLineService.START_CMD);
                String endId = en.get(FlowLineService.END_CMD);
                FlowLineEntity flowLineEntity = new FlowLineEntity();
                flowLineEntity.setStart(idCanvas.get(startId));
                flowLineEntity.setEnd(idCanvas.get(endId));
                flowLineEntity.setFlowEntity(flowEntity);
                data.add(flowLineEntity);
            }
            flowLineRepository.saveAll(data);
        }

    }


    public void debugFlow(String id, String data) throws BusException {


    }

}
