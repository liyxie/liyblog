package com.liy.controller;

import com.liy.common.ResponseResult;
import com.liy.service.ApiCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 分类处理器
 * @author blue
 */
@Tag(name = "分类API-V1")
@RestController
@RequestMapping("v1/category")
@RequiredArgsConstructor
public class ApiCategoryController {

    private final ApiCategoryService categoryService;

    @RequestMapping(value = "/",method = RequestMethod.GET)
    @Operation(description = "分类列表", summary = "分类列表")
    @Schema(description = "分类列表")
    public ResponseResult selectCategoryList(){
        return categoryService.selectCategoryList();
    }
}