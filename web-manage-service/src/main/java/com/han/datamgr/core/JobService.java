package com.han.datamgr.core;

import com.han.datamgr.exception.BusException;
import com.han.datamgr.vo.JobVO;

/**
 * @author: Hanl
 * @date :2019/9/29
 * @desc:
 */
public interface JobService {

    /**
     *
     * @param jobVO
     * @throws BusException
     */
    public void crateJob(JobVO jobVO) throws BusException;

    /**
     *
     * @param jobVO
     * @throws BusException
     */
    public void updateJob(JobVO jobVO) throws BusException;

    /**
     *
     * @param jobVO
     * @throws BusException
     */
    public void removeJob(JobVO jobVO) throws BusException;

    /**
     * 
     * @throws BusException
     */
    public void queryJobs() throws BusException;

}
