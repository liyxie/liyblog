package com.liy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liy.common.ResponseResult;
import com.liy.entity.JobLog;

import java.util.List;

/**
 * <p>
 * 定时任务调度日志表 服务类
 * </p>
 *
 * @author blue
 * @since 2021-12-08
 */
public interface JobLogService extends IService<JobLog> {

    /**
     * 分页
     * @param jobName
     * @param jobGroup
     * @param status
     * @param startTime
     * @param endTime
     * @param jobId
     * @return
     */
    ResponseResult selectJobLogPage(String jobName, String jobGroup, String status, String startTime,
                            String endTime, Long jobId);

    /**
     * 删除日志
     * @param ids
     * @return
     */
    ResponseResult deleteJobLog(List<Long> ids);

    /**
     * 清空日志
     * @return
     */
    ResponseResult cleanJobLog();
}
