package com.liy.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import com.liy.annotation.OperationLogger;
import com.liy.common.ResponseResult;
import com.liy.service.AdminLogService;
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
 * @since 2021-11-10
 */
@RestController
@RequestMapping("/system/adminLog")
@RequiredArgsConstructor
@Tag(name = "操作日志管理")
public class AdminLogController {

    private final AdminLogService adminLogService;

    @GetMapping(value = "/list")
    @Operation(summary = "操作日志列表")
    public ResponseResult selectAdminLogPage() {
        return adminLogService.selectAdminLogPage();
    }

    @DeleteMapping(value = "/delete")
    @OperationLogger(value = "删除操作日志")
    @SaCheckPermission("system:adminLog:delete")
    @Operation(summary = "删除操作日志")
    public ResponseResult deleteAdminLog(@RequestBody List<Long> ids) {
        return adminLogService.deleteAdminLog(ids);
    }
}

