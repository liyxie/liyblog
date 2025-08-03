package com.liy.controller;

import com.liy.common.ResponseResult;
import com.liy.service.ApiCategoryService;
import com.liy.service.ApiToolService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 分类处理器
 * @author liy
 */
@Schema(title = "工具链接API-V1")
@RestController
@RequestMapping("v1/tool")
@RequiredArgsConstructor
public class ApiToolController {

    private final ApiToolService apiToolService;

    @RequestMapping(value = "/",method = RequestMethod.GET)
    @Schema(description = "工具列表", httpMethod = "GET", response = ResponseResult.class, notes = "工具列表")
    public ResponseResult selectToolList(){
        return apiToolService.selectToolList();
    }
}
