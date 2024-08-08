package com.liy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liy.common.ResponseResult;
import com.liy.entity.Job;
import com.liy.enums.TaskException;
import org.quartz.SchedulerException;

import java.util.List;

/**
 * <p>
 * 定时任务调度表 服务类
 * </p>
 *
 * @author blue
 * @since 2021-12-08
 */
public interface JobService extends IService<Job> {

    /**
     * 分页
     * @param jobName
     * @param jobGroup
     * @param status
     * @return
     */
    ResponseResult selectJobPage(String jobName, String jobGroup, String status);

    /**
     * 详情
     * @param jobId
     * @return
     */
    ResponseResult selectJobById(Long jobId);

    /**
     * 添加
     * @param job
     * @return
     * @throws SchedulerException
     * @throws TaskException
     */
    ResponseResult addJob(Job job) throws SchedulerException, TaskException;

    /**
     * 修改
     * @param job
     * @return
     * @throws SchedulerException
     * @throws TaskException
     */
    ResponseResult updateJob(Job job) throws SchedulerException, TaskException;

    /**
     * 删除
     * @param ids
     * @return
     */
    ResponseResult deleteJob(List<Long> ids);


    /**
     * 立即执行
     * @param job
     * @return
     */
    ResponseResult runJob(Job job);

    /**
     * 修改状态
     * @param job
     * @return
     * @throws SchedulerException
     */
    ResponseResult changeStatus(Job job) throws SchedulerException;


    /**
     * 暂停任务
     * @param job
     * @return
     */
    ResponseResult pauseJob(Job job) throws SchedulerException;

}
