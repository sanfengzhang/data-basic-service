package com.han.datamgr.repository;

import com.han.datamgr.entity.JobEntity;
import com.han.datamgr.repository.support.BaseRepository;
import org.springframework.stereotype.Repository;

/**
 * @author: Hanl
 * @date :2019/9/30
 * @desc:
 */
@Repository
public interface JobRepository extends BaseRepository<JobEntity, String> {

}
