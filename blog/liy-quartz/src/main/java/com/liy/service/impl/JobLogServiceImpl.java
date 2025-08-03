package com.liy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liy.service.JobLogService;
import com.liy.common.ResponseResult;
import com.liy.entity.JobLog;
import com.liy.mapper.JobLogMapper;
import com.liy.utils.PageUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 定时任务调度日志表 服务实现类
 * </p>
 *
 * @author blue
 * @since 2021-12-08
 */
@Service
public class JobLogServiceImpl extends ServiceImpl<JobLogMapper, JobLog> implements JobLogService {

    /**
     * 任务日志列表
     * @param jobName
     * @param jobGroup
     * @param status
     * @param startTime
     * @param endTime
     * @param jobId
     * @return
     */
    @Override
    public ResponseResult selectJobLogPage(String jobName, String jobGroup, String status, String startTime,
                                   String endTime, Long jobId) {
        LambdaQueryWrapper<JobLog> queryWrapper = new LambdaQueryWrapper<JobLog>()
                .eq(jobId != null, JobLog::getJobId,jobId)
                .like(StringUtils.isNotBlank(jobName),JobLog::getJobName,jobName)
                .like(StringUtils.isNotBlank(jobGroup), JobLog::getJobGroup,jobGroup)
                .eq(StringUtils.isNotBlank(status), JobLog::getStatus,status)
                .between(StringUtils.isNotBlank(startTime), JobLog::getStartTime,startTime,endTime)
                .orderByDesc(JobLog::getCreateTime);

        Page<JobLog> page = baseMapper.selectPage(new Page<>(PageUtil.getPageNo(), PageUtil.getPageSize()), queryWrapper);
        return ResponseResult.success(page);
    }

    /**
     * 批量删除日志
     * @param ids
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult deleteJobLog(List<Long> ids) {
        baseMapper.deleteByIds(ids);
        return ResponseResult.success();
    }

    /**
     * 清空日志
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult cleanJobLog() {
        baseMapper.clean();
        return ResponseResult.success();
    }

}
