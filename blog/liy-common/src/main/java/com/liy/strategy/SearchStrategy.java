package com.liy.strategy;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liy.vo.article.ApiArticleSearchVO;

import java.util.List;

/**
 * @author blue
 * @date 2022/1/19
 * @apiNote 搜索策略
 */
public interface SearchStrategy {
    /**
     * 搜索文章
     *
     * @param keywords 关键字
     * @return {@link List<  ApiArticleSearchVO  >} 文章列表
     */
    Page<ApiArticleSearchVO> searchArticle(String keywords);
}
