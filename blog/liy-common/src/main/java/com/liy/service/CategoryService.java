package com.liy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liy.common.ResponseResult;
import com.liy.entity.Category;

import java.util.List;

/**
 * <p>
 * 博客分类表 服务类
 * </p>
 *
 * @author blue
 * @since 2021-12-29
 */
public interface CategoryService extends IService<Category> {

    /**
     * 分类列表
     * @param name 分类名
     * @return
     */
    ResponseResult selectCategoryPage(String name);


    /**
     * 添加分类
     * @param category 分类对象
     * @return
     */
    ResponseResult addCategory(Category category);

    /**
     * 修改分类
     * @param category 分类对象
     * @return
     */
    ResponseResult updateCategory(Category category);


    /**
     * 批量删除分类
     * @param list 分类对象集合
     * @return
     */
    ResponseResult deleteCategory(List<Long> list);


}
