package com.liy.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import com.liy.annotation.OperationLogger;
import com.liy.common.ResponseResult;
import com.liy.service.UserLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 日志表 前端控制器
 * </p>
 *
 * @author blue
 * @since 2021-11-09
 */
@RestController
@RequestMapping("/system/userLog")
@RequiredArgsConstructor
@Schema(title = "用户日志管理")
public class UserLogController {

    private final UserLogService userLogService;

    @GetMapping(value = "/list")
    @Schema(description = "用户日志列表", httpMethod = "GET", response = ResponseResult.class, notes = "用户日志列表")
    public ResponseResult selectUserLogPage() {
        return userLogService.selectUserLogPage();
    }

    @DeleteMapping(value = "/delete")
    @SaCheckPermission("system:userLog:delete")
    @OperationLogger(value = "删除用户日志")
    @Schema(description = "删除用户日志", httpMethod = "DELETE", response = ResponseResult.class, notes = "删除用户日志")
    public ResponseResult deleteBatch(@RequestBody List<Long> ids) {
        return userLogService.deleteUserLog(ids);
    }
}

