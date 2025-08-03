package com.liy.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.liy.annotation.OperationLogger;
import com.liy.common.ResponseResult;
import com.liy.entity.Job;
import com.liy.enums.TaskException;
import com.liy.service.JobService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.quartz.SchedulerException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 定时任务调度表 前端控制器
 * </p>
 *
 * @author blue
 * @since 2021-12-08
 */
@RestController
@RequestMapping("/system/job")
@Schema(title = "定时任务管理")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    @GetMapping(value = "/list")
    @Schema(description = "定时任务列表", httpMethod = "GET", response = ResponseResult.class, notes = "定时任务列表")
    public ResponseResult selectJobPage(String jobName, String jobGroup, String status) {
        return jobService.selectJobPage(jobName,jobGroup,status);
    }

    @GetMapping(value = "/info")
    @Schema(description = "定时任务详情", httpMethod = "GET", response = ResponseResult.class, notes = "定时任务详情")
    public ResponseResult selectJobById(Long jobId) {
        return jobService.selectJobById(jobId);
    }

    @PostMapping(value = "/add")
    @SaCheckPermission("system:job:add")
    @Schema(description = "添加定时任务", httpMethod = "POST", response = ResponseResult.class, notes = "添加定时任务")
    @OperationLogger(value = "添加定时任务")
    public ResponseResult addJob(@RequestBody Job job) throws SchedulerException, TaskException {
        return jobService.addJob(job);
    }

    @PutMapping(value = "/update")
    @SaCheckPermission("system:job:update")
    @Schema(description = "修改定时任务", httpMethod = "PUT", response = ResponseResult.class, notes = "修改定时任务")
    @OperationLogger(value = "修改定时任务")
    public ResponseResult updateJob(@RequestBody Job job) throws SchedulerException, TaskException {
        return jobService.updateJob(job);
    }

    @DeleteMapping(value = "/delete")
    @SaCheckPermission("system:job:delete")
    @Schema(description = "批量删除定时任务", httpMethod = "DELETE", response = ResponseResult.class, notes = "批量删除定时任务")
    @OperationLogger(value = "批量删除定时任务")
    public ResponseResult deleteJob(@RequestBody List<Long> ids) {
        return jobService.deleteJob(ids);
    }

    @PostMapping(value = "/run")
    @SaCheckPermission("system:job:run")
    @Schema(description = "立即执行", httpMethod = "POST", response = ResponseResult.class, notes = "立即执行")
    @OperationLogger(value = "立即执行")
    public ResponseResult runJob(@RequestBody Job job) {
        return jobService.runJob(job);
    }

    @PostMapping(value = "/change")
    @SaCheckPermission("system:job:change")
    @Schema(description = "修改状态", httpMethod = "POST", response = ResponseResult.class, notes = "修改状态")
    @OperationLogger(value = "修改状态")
    public ResponseResult changeStatus(@RequestBody Job job) throws SchedulerException {
        return jobService.changeStatus(job);
    }
}

