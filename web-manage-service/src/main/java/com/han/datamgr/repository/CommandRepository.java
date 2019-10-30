package com.han.datamgr.repository;

import com.han.datamgr.entity.CommandEntity;
import com.han.datamgr.repository.support.BaseRepository;
import org.springframework.stereotype.Repository;

/**
 * @author: Hanl
 * @date :2019/9/30
 * @desc:
 */
@Repository
public interface CommandRepository extends BaseRepository<CommandEntity,String> {
}
