package com.liy.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.liy.common.ResponseResult;
import com.liy.service.GenerateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
@RequiredArgsConstructor
@RequestMapping("/system/generate")
public class GenerateController {

    private final GenerateService generateService;


    @GetMapping(value = "/list")
    public ResponseResult list() throws IOException {
        return generateService.selectListTables();
    }

    @SaCheckPermission("system:generate:preview")
    @GetMapping(value = "/preview/{tableName}")
    public ResponseResult index(@PathVariable String tableName) throws IOException {
        return generateService.preview(tableName);
    }

    /**
     * 生成代码（下载方式）
     */
    @GetMapping("/download")
    @SaCheckPermission("system:generate:download")
    public ResponseResult download(String tableName,String downloadPath) throws IOException
    {
        generateService.download(tableName,downloadPath);
        return ResponseResult.success();
    }
}
