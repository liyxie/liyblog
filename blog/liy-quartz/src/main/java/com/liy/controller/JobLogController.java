package com.liy.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import com.liy.common.ResponseResult;
import com.liy.service.JobLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 定时任务调度日志表 前端控制器
 * </p>
 *
 * @author blue
 * @since 2021-12-08
 */
@RestController
@RequestMapping("/system/jobLog")
@Tag(name = "定时任务调度日志管理")
@RequiredArgsConstructor
public class JobLogController {

    private final JobLogService jobLogService;

    @GetMapping(value = "/list")
    @Operation(summary = "定时任务日志列表")
    public ResponseResult selectJobLogPage(String jobName, String jobGroup, String status, String startTime,
                                String endTime, Long jobId) {
        return jobLogService.selectJobLogPage(jobName,jobGroup,status,startTime,endTime,jobId);
    }

    @DeleteMapping(value = "/delete")
    @SaCheckPermission("system:jobLog:delete")
    @Operation(summary = "批量删除日志列表")
    public ResponseResult deleteBatch(@RequestBody List<Long> ids) {
        return jobLogService.deleteJobLog(ids);
    }

    @GetMapping(value = "/clean")
    @SaCheckPermission("system:jobLog:clean")
    @Operation(summary = "清空日志列表")
    public ResponseResult clean() {
        return jobLogService.cleanJobLog();
    }
}