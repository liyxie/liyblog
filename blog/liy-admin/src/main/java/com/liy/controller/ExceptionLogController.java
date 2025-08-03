package com.liy.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import com.liy.annotation.OperationLogger;
import com.liy.common.ResponseResult;
import com.liy.service.ExceptionLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author blue
 * @since 2021-11-11
 */
@RestController
@RequestMapping("/system/exceptionLog")
@RequiredArgsConstructor
@Tag(name = "异常日志管理")
public class ExceptionLogController {

    private final ExceptionLogService exceptionLogService;

    @GetMapping(value = "/list")
    @Operation(summary = "异常日志列表")
    public ResponseResult selectExceptionLogPage() {
        return exceptionLogService.selectExceptionLogPage();
    }

    @DeleteMapping(value = "/delete")
    @SaCheckPermission("system:exceptionLog:delete")
    @OperationLogger(value = "删除异常日志")
    @Operation(summary = "删除异常日志")
    public ResponseResult deleteExceptionLog(@RequestBody List<Long> ids) {
        return exceptionLogService.deleteExceptionLog(ids);
    }
}

