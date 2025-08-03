package com.liy.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import com.liy.annotation.OperationLogger;
import com.liy.common.ResponseResult;
import com.liy.entity.WebConfig;
import com.liy.service.WebConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 网站配置表 前端控制器
 * </p>
 *
 * @author blue
 * @since 2021-11-27
 */
@RestController
@RequestMapping("/system/webConfig")
@Tag(name = "网站配置管理")
@RequiredArgsConstructor
public class WebConfigController {

    private final WebConfigService webConfigService;

    @GetMapping(value = "/")
    @Operation(summary = "网站配置列表")
    public ResponseResult getWebConfig() {
        return webConfigService.getWebConfig();
    }

    @PutMapping(value = "/update")
    @SaCheckPermission("system:webConfig:update")
    @Operation(summary = "修改网站配置")
    @OperationLogger(value = "修改网站配置")
    public ResponseResult updateWebConfig(@RequestBody WebConfig webConfig) {
        return webConfigService.updateWebConfig(webConfig);
    }
}

