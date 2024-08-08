package com.liy.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.liy.annotation.OperationLogger;
import com.liy.common.ResponseResult;
import com.liy.dto.article.ArticleDTO;
import com.liy.entity.Article;
import com.liy.service.ArticleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/article")
@RequiredArgsConstructor
@Api(tags = "后台文章管理")
public class ArticlesController {

    private final ArticleService articleService;


    @GetMapping(value = "/list")
    @ApiOperation(value = "文章列表", httpMethod = "GET", response = ResponseResult.class, notes = "文章列表")
    public ResponseResult selectArticlePage(String title,Integer tagId,Integer categoryId,Integer isPublish) {
        return articleService.selectArticlePage(title,tagId,categoryId,isPublish);
    }

    @GetMapping(value = "/info/{id}")
    @ApiOperation(value = "文章详情", httpMethod = "GET", response = ResponseResult.class, notes = "文章详情")
    public ResponseResult selectArticleById(@PathVariable Long id) {
        return articleService.selectArticleById(id);
    }

    @PostMapping(value = "/add")
    @SaCheckPermission("system:article:add")
    @OperationLogger(value = "保存文章")
    @ApiOperation(value = "保存文章", httpMethod = "POST", response = ResponseResult.class, notes = "保存文章")
    public ResponseResult addArticle(@RequestBody ArticleDTO article) {
        return  articleService.addArticle(article);
    }

    @PutMapping(value = "/update")
    @SaCheckPermission("system:article:update")
    @OperationLogger(value = "修改文章")
    @ApiOperation(value = "修改文章", httpMethod = "PUT", response = ResponseResult.class, notes = "修改文章")
    public ResponseResult updateArticle(@RequestBody ArticleDTO article) {
        return articleService.updateArticle(article);
    }


    @DeleteMapping(value = "/delete")
    @SaCheckPermission("system:article:delete")
    @OperationLogger(value = "删除文章")
    @ApiOperation(value = "删除文章", httpMethod = "DELETE", response = ResponseResult.class, notes = "删除文章")
    public ResponseResult deleteBatchArticle(@RequestBody List<Long> ids) {
        return articleService.deleteBatchArticle(ids);
    }

    @PutMapping(value = "/top")
    @SaCheckPermission("system:article:top")
    @OperationLogger(value = "置顶文章")
    @ApiOperation(value = "置顶文章", httpMethod = "PUT", response = ResponseResult.class, notes = "置顶文章")
    public ResponseResult topArticle(@RequestBody ArticleDTO article) {
        return articleService.topArticle(article);
    }

    @PutMapping(value = "/toggleArticlePublication")
    @SaCheckPermission("system:article:toggleArticlePublication")
    @OperationLogger(value = "发布或下架文章")
    @ApiOperation(value = "发布或下架文章", httpMethod = "PUT", response = ResponseResult.class, notes = "发布或下架文章")
    public ResponseResult toggleArticlePublication(@RequestBody Article article) {
        return articleService.toggleArticlePublication(article);
    }

    @PostMapping(value = "/seo")
    @SaCheckPermission("system:article:seo")
    @OperationLogger(value = "批量文章SEO")
    @ApiOperation(value = "批量文章SEO", httpMethod = "POST", response = ResponseResult.class, notes = "文章SEO")
    public ResponseResult seoArticle(@RequestBody List<Long> ids) {
        return articleService.seoArticle(ids);
    }

    @GetMapping(value = "/reptile")
    @SaCheckPermission("system:article:reptile")
    @OperationLogger(value = "文章爬虫")
    @ApiOperation(value = "文章爬虫", httpMethod = "GET", response = ResponseResult.class, notes = "文章爬虫")
    public ResponseResult reptile(String url) {
        return articleService.reptile(url);
    }

    @GetMapping(value = "/randomImg")
    @ApiOperation(value = "随机获取一张图片", httpMethod = "GET", response = ResponseResult.class, notes = "随机获取一张图片")
    public ResponseResult randomImg() {
        return articleService.randomImg();
    }

}
