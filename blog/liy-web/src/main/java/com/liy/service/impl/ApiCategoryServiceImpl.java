package com.liy.service.impl;

import com.liy.service.ApiCategoryService;
import com.liy.common.ResponseResult;
import com.liy.mapper.CategoryMapper;
import com.liy.vo.category.ApiCategoryListVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApiCategoryServiceImpl implements ApiCategoryService {

    private final CategoryMapper categoryMapper;

    /**
     * 首页分类列表
     * @return
     */
    @Override
    public ResponseResult selectCategoryList() {
        List<ApiCategoryListVO> list = categoryMapper.selectCategoryListApi();
        return ResponseResult.success(list);
    }
}
