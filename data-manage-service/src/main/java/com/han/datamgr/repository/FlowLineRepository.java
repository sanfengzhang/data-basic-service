package com.han.datamgr.repository;

import com.han.datamgr.entity.FlowLineEntity;
import com.han.datamgr.repository.support.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author: Hanl
 * @date :2019/10/19
 * @desc:
 */
@Repository
public interface FlowLineRepository extends BaseRepository<FlowLineEntity, String> {

    @Query(nativeQuery = true, value = "select * from  flow_cmd_line where flow_id=:flowId and line_status in(1,2) order by line_status asc")
    public List<FlowLineEntity> queryFlowLineByLineStatus(@Param("flowId") String flowId);
}
