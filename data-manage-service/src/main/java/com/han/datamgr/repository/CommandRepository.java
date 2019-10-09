package com.han.datamgr.repository;

import com.han.datamgr.entity.CommandEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author: Hanl
 * @date :2019/9/30
 * @desc:
 */
@Repository
public interface CommandRepository extends JpaRepository<CommandEntity,String> {
}