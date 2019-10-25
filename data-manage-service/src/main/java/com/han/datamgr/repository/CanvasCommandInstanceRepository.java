package com.han.datamgr.repository;

import com.han.datamgr.entity.CanvasCommandInstanceEntity;
import com.han.datamgr.repository.support.BaseRepository;

import java.util.Set;
import java.util.List;

/**
 * @author: Hanl
 * @date :2019/10/24
 * @desc:
 */
public interface CanvasCommandInstanceRepository extends BaseRepository<CanvasCommandInstanceEntity,String> {

    public List<CanvasCommandInstanceEntity> findAllByIdIn(Set<String> ids);

    public void deleteAllByIdIn(Set<String> ids);
}
