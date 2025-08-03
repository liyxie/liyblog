package com.liy.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import com.liy.annotation.OperationLogger;
import com.liy.common.ResponseResult;
import com.liy.dto.system.UpdateSystemConfigDTO;
import com.liy.entity.SystemConfig;
import com.liy.service.SystemConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 系统配置表 前端控制器
 * </p>
 *
 * @author blue
 * @since 2021-11-25
 */
@RestController
@RequestMapping("/system/config")
@Tag(name = "系统配置管理")
@RequiredArgsConstructor
public class SystemConfigController {

    private final SystemConfigService systemConfigService;

    @RequestMapping(value = "/getConfig",method = RequestMethod.GET)
    @Operation(summary = "查询系统配置")
    public ResponseResult getSystemConfig(){
        return systemConfigService.getSystemConfig();
    }

    @RequestMapping(value = "/update",method = RequestMethod.PUT)
    @SaCheckPermission("system:config:update")
    @Operation(summary = "修改系统配置")
    @OperationLogger(value = "修改系统配置")
    public ResponseResult updateSystemConfig(@RequestBody UpdateSystemConfigDTO systemConfig){
        return systemConfigService.updateSystemConfig(systemConfig);
    }
}

