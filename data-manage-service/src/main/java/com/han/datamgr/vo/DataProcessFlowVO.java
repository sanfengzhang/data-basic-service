package com.han.datamgr.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.han.datamgr.entity.CommandInstanceEntity;
import com.han.datamgr.entity.DataProcessFlowEntity;
import com.han.datamgr.entity.FlowLineEntity;
import lombok.Data;
import lombok.ToString;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.*;

/**
 * @author: Hanl
 * @date :2019/9/27
 * @desc:
 */
@Data
@ToString
public class DataProcessFlowVO implements Serializable {

    @NotBlank(message = "流程名字不能为空")
    private String dataProcessFlowName;

    private String loadExternalLibsPath;

    private DataProcessFlowEntity flowEntity;

    private List<Map<String, String>> lineList = new ArrayList<>();

    private List<CommandInstanceEntity> nodeList = new ArrayList<>();


    public void formEntityToLineList() {
        if (null == flowEntity) {
            return;
        }
        Set<FlowLineEntity> flowLineEntitySet = flowEntity.getFlowLineSet();
        for (FlowLineEntity flowLineEntity : flowLineEntitySet) {
            Map<String, String> lineMap = new HashMap<>();
            String from = flowLineEntity.getStart().getId();
            String to = flowLineEntity.getEnd().getId();
            lineMap.put("from", from);
            lineMap.put("to", to);
            lineList.add(lineMap);
        }
    }

    public void fromEntityToNodeList() {
        Set<FlowLineEntity> flowLineEntitySet = flowEntity.getFlowLineSet();
        if (!CollectionUtils.isEmpty(flowLineEntitySet)) {
            Map<String, CommandInstanceEntity> idInstance = new HashMap<>();
            for (FlowLineEntity flowLineEntity : flowLineEntitySet) {
                String fromId = flowLineEntity.getStart().getId();
                String toId = flowLineEntity.getEnd().getId();
                if (!idInstance.containsKey(fromId)) {
                    idInstance.put(fromId, flowLineEntity.getStart());
                }
                if (!idInstance.containsKey(toId)) {
                    idInstance.put(toId, flowLineEntity.getEnd());
                }
            }
            nodeList.addAll(idInstance.values());
        }
    }
}
