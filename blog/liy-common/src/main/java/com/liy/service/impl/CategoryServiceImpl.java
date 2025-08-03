package com.liy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liy.service.CategoryService;
import com.liy.common.ResponseResult;
import com.liy.common.ResultCode;
import com.liy.entity.Article;
import com.liy.entity.Category;
import com.liy.exception.BusinessException;
import com.liy.mapper.ArticleMapper;
import com.liy.mapper.CategoryMapper;
import com.liy.utils.PageUtil;
import com.liy.vo.category.SystemCategoryListVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 博客分类表 服务实现类
 * </p>
 *
 * @author blue
 * @since 2021-12-29
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    private final ArticleMapper articleMapper;

    /**
     * 分类列表
     * @param name 分类名
     * @return
     */
    @Override
    public ResponseResult selectCategoryPage(String name) {
        Page<SystemCategoryListVO> categoryPage = baseMapper.selectCategory(new Page<>(PageUtil.getPageNo(), PageUtil.getPageSize()), name);
        return ResponseResult.success(categoryPage);
    }

    /**
     * 添加分类
     * @param category 分类对象
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult addCategory(Category category) {
        Category vo = baseMapper.selectOne(new LambdaQueryWrapper<Category>().eq(Category::getName, category.getName()));
        if (vo != null) {
            throw new BusinessException(ResultCode.CATEGORY_IS_EXIST);
        }
        baseMapper.insert(category);
        return ResponseResult.success();
    }

    /**
     * 修改分类
     * @param category 分类对象
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult updateCategory(Category category) {
        Category vo = baseMapper.selectOne(new LambdaQueryWrapper<Category>().eq(Category::getName, category.getName()));
        if (vo!= null && !vo.getId().equals(category.getId())) {
            throw new BusinessException(ResultCode.CATEGORY_IS_EXIST);
        }

        baseMapper.updateById(category);
        return ResponseResult.success();
    }

    /**
     * 批量删除分类
     * @param list
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult deleteCategory(List<Long> list) {
        List<Long> ids = new ArrayList<>();
        for (Long id : list) {
            Long count = articleMapper.selectCount(new LambdaQueryWrapper<Article>().eq(Article::getCategoryId, id));
            if (count > 0) {
                throw new BusinessException( id + " 分类下有文章，不能删除");
            }
            ids.add(id);
        }

        baseMapper.deleteByIds(ids);
        return ResponseResult.success();
    }

}
