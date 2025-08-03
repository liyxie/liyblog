package com.liy.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liy.common.ResponseResult;
import com.liy.entity.Job;
import com.liy.entity.User;
import com.liy.enums.ScheduleConstants;
import com.liy.enums.TaskException;
import com.liy.exception.BusinessException;
import com.liy.mapper.JobMapper;
import com.liy.mapper.UserMapper;
import com.liy.utils.CronUtil;
import com.liy.utils.ScheduleUtil;
import com.liy.service.JobService;
import com.liy.utils.PageUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.quartz.JobDataMap;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import jakarta.annotation.PostConstruct;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 定时任务调度表 服务实现类
 * </p>
 *
 * @author blue
 * @since 2021-12-08
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JobServiceImpl extends ServiceImpl<JobMapper, Job> implements JobService {

    private final Scheduler scheduler;

    private final UserMapper userMapper;

    /**
     * 项目启动时，初始化定时器 主要是防止手动修改数据库导致未同步到定时任务处理
     * （注：不能手动修改数据库ID和任务组名，否则会导致脏数据）
     */
    @PostConstruct
    public void init() throws SchedulerException, TaskException {
        scheduler.clear();
        List<Job> jobList = baseMapper.selectList(null);
        for (Job job : jobList) {
            ScheduleUtil.createScheduleJob(scheduler, job);
        }
    }

    /**
     * 任务列表
     *
     * @return
     */
    @Override
    public ResponseResult selectJobPage(String jobName, String jobGroup, String status) {
        LambdaQueryWrapper<Job> queryWrapper = new LambdaQueryWrapper<Job>()
                .like(StringUtils.isNotBlank(jobName), Job::getJobName,jobName)
                .eq(StringUtils.isNotBlank(jobGroup), Job::getJobGroup,jobGroup)
                .eq(StringUtils.isNotBlank(status), Job::getStatus,status);

        Page<Job> sysJobPage = baseMapper.selectPage(new Page<>(PageUtil.getPageNo(), PageUtil.getPageSize()), queryWrapper);
        return ResponseResult.success(sysJobPage);
    }

    /**
     *  任务详情
     * @param jobId
     * @return
     */
    @Override
    public ResponseResult selectJobById(Long jobId) {
        Job job = baseMapper.selectById(jobId);
        Date nextExecution = CronUtil.getNextExecution(job.getCronExpression());
        job.setNextValidTime(nextExecution);
        return ResponseResult.success(job);
    }

    /**
     * 新增任务
     *
     * @param job 调度信息
     * @return
     * @throws SchedulerException
     * @throws TaskException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult addJob(Job job) throws SchedulerException, TaskException {
        checkCronIsValid(job);

        User user = userMapper.selectById(StpUtil.getLoginIdAsString());
        job.setCreateBy(user.getUsername());
        int row = baseMapper.insert(job);
        if (row > 0) ScheduleUtil.createScheduleJob(scheduler, job);

        return ResponseResult.success();
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult updateJob(Job job) throws SchedulerException, TaskException {
        checkCronIsValid(job);

        User user = userMapper.selectById(StpUtil.getLoginIdAsString());
        job.setUpdateBy(user.getUsername());
        Job properties = baseMapper.selectById(job.getJobId());
        int row = baseMapper.updateById(job);
        if (row > 0) updateSchedulerJob(job, properties.getJobGroup());

        return ResponseResult.success();
    }


    /**
     * 批量删除任务
     *
     * @param ids
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult deleteJob(List<Long> ids) {
        baseMapper.deleteByIds(ids);
        return ResponseResult.success();
    }

    /**
     * 暂停任务
     *
     * @param job 调度信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult pauseJob(Job job) throws SchedulerException {
        Long jobId = job.getJobId();
        String jobGroup = job.getJobGroup();
        job.setStatus(ScheduleConstants.Status.PAUSE.getValue());
        int rows = baseMapper.updateById(job);
        if (rows > 0) {
            scheduler.pauseJob(ScheduleUtil.getJobKey(jobId, jobGroup));
        }
        return ResponseResult.success();
    }

    /**
     * 立即运行任务
     *
     * @param job 调度信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult runJob(Job job) {
        try {
            Long jobId = job.getJobId();
            String jobGroup = job.getJobGroup();
            // 参数
            JobDataMap dataMap = new JobDataMap();
            dataMap.put(ScheduleConstants.TASK_PROPERTIES, job);
            scheduler.triggerJob(ScheduleUtil.getJobKey(jobId, jobGroup), dataMap);
            return ResponseResult.success();
        } catch (Exception e) {
            throw new BusinessException("定时任务运行失败！失败原因:" + e.getMessage());
        }
    }

    /**
     * 任务调度状态修改
     *
     * @param job 调度信息
     */

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult changeStatus(Job job) throws SchedulerException {
        String status = job.getStatus();
        Long jobId = job.getJobId();
        String jobGroup = job.getJobGroup();
        int row = baseMapper.updateById(job);
        if (row > 0){
            if (ScheduleConstants.Status.NORMAL.getValue().equals(status)) {
                scheduler.resumeJob(ScheduleUtil.getJobKey(jobId, jobGroup));
            } else if (ScheduleConstants.Status.PAUSE.getValue().equals(status)) {
                scheduler.pauseJob(ScheduleUtil.getJobKey(jobId, jobGroup));
            }
        }

        return ResponseResult.success();
    }


    //---------自定义方法开始--------
    /**
     * 验证cron
     * @param job
     */
    private void checkCronIsValid(Job job) {
        boolean valid = CronUtil.isValid(job.getCronExpression());
        Assert.isTrue(valid,"Cron表达式无效!");
    }

    /**
     * 更新任务
     * @param job      任务对象
     * @param jobGroup 任务组名
     */
    public void updateSchedulerJob(Job job, String jobGroup) throws SchedulerException, TaskException {
        Long jobId = job.getJobId();
        // 判断是否存在
        JobKey jobKey = ScheduleUtil.getJobKey(jobId, jobGroup);
        if (scheduler.checkExists(jobKey)) {
            // 防止创建时存在数据问题 先移除，然后在执行创建操作
            scheduler.deleteJob(jobKey);
        }
        ScheduleUtil.createScheduleJob(scheduler, job);
    }
}
