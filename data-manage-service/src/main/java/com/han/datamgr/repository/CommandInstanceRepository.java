package com.han.datamgr.repository;

import com.han.datamgr.entity.CommandInstanceEntity;
import com.han.datamgr.repository.support.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author: Hanl
 * @date :2019/9/30
 * @desc:
 */
@Repository
public interface CommandInstanceRepository extends BaseRepository<CommandInstanceEntity, String> {

}
