package com.liy.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import com.liy.annotation.OperationLogger;
import com.liy.common.ResponseResult;
import com.liy.entity.Category;
import com.liy.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = "分类管理")
public class CategoryController {

    private final CategoryService categoryService;

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @ApiOperation(value = "分类列表", httpMethod = "GET", response = ResponseResult.class, notes = "分类列表")
    public ResponseResult selectCategoryPage(String name){
        return categoryService.selectCategoryPage(name);
    }


    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @SaCheckPermission("system:category:add")
    @OperationLogger(value = "新增分类")
    @ApiOperation(value = "新增分类", httpMethod = "POST", response = ResponseResult.class, notes = "新增分类")
    public ResponseResult addCategory(@RequestBody Category category){
        return categoryService.addCategory(category);
    }

    @RequestMapping(value = "/update",method = RequestMethod.PUT)
    @SaCheckPermission("system:category:update")
    @ApiOperation(value = "修改分类", httpMethod = "PUT", response = ResponseResult.class, notes = "修改分类")
    @OperationLogger(value = "修改分类")
    public ResponseResult update(@RequestBody Category category){
        return categoryService.updateCategory(category);
    }

    @RequestMapping(value = "/delete",method = RequestMethod.DELETE)
    @SaCheckPermission("system:category:delete")
    @ApiOperation(value = "批量删除分类", httpMethod = "DELETE", response = ResponseResult.class, notes = "批量删除分类")
    @OperationLogger(value = "批量删除分类")
    public ResponseResult deleteCategory(@RequestBody List<Long> list){
        return categoryService.deleteCategory(list);
    }

}

