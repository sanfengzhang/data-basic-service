package com.han.datamgr.repository;

import com.han.datamgr.entity.DataProcessFlowEntity;
import com.han.datamgr.repository.support.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author: Hanl
 * @date :2019/9/30
 * @desc:
 */
@Repository
public interface DataProcessFlowRepository extends BaseRepository<DataProcessFlowEntity, String> {

    public Optional<DataProcessFlowEntity> findByDataProcessFlowName(String flowName);
}
