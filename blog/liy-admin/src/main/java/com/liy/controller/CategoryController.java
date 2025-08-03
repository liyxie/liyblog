package com.liy.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import com.liy.annotation.OperationLogger;
import com.liy.common.ResponseResult;
import com.liy.entity.Category;
import com.liy.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 博客分类表 前端控制器
 * </p>
 *
 * @author blue
 * @since 2021-12-29
 */
@RestController
@RequestMapping("/system/category")
@RequiredArgsConstructor
@Tag(name = "分类管理")
public class CategoryController {

    private final CategoryService categoryService;

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @Operation(summary = "分类列表")
    public ResponseResult selectCategoryPage(String name){
        return categoryService.selectCategoryPage(name);
    }


    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @SaCheckPermission("system:category:add")
    @OperationLogger(value = "新增分类")
    @Operation(summary = "新增分类")
    public ResponseResult addCategory(@RequestBody Category category){
        return categoryService.addCategory(category);
    }

    @RequestMapping(value = "/update",method = RequestMethod.PUT)
    @SaCheckPermission("system:category:update")
    @Operation(summary = "修改分类")
    @OperationLogger(value = "修改分类")
    public ResponseResult update(@RequestBody Category category){
        return categoryService.updateCategory(category);
    }

    @RequestMapping(value = "/delete",method = RequestMethod.DELETE)
    @SaCheckPermission("system:category:delete")
    @Operation(summary = "批量删除分类")
    @OperationLogger(value = "批量删除分类")
    public ResponseResult deleteCategory(@RequestBody List<Long> list){
        return categoryService.deleteCategory(list);
    }

}

