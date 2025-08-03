package com.liy.controller;

import com.liy.common.ResponseResult;
import com.liy.service.ApiCategoryService;
import com.liy.service.ApiToolService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 分类处理器
 * @author liy
 */
@Tag(name = "工具链接API-V1")
@RestController
@RequestMapping("v1/tool")
@RequiredArgsConstructor
public class ApiToolController {

    private final ApiToolService apiToolService;

    @RequestMapping(value = "/",method = RequestMethod.GET)
    @Operation(description = "工具列表", summary = "工具列表")
    public ResponseResult selectToolList(){
        return apiToolService.selectToolList();
    }
}