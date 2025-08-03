package com.liy.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.liy.annotation.AccessLimit;
import com.liy.common.ResponseResult;
import com.liy.service.ApiCollectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 * @author blue
 * @date 2022/1/13
 * @apiNote
 */
@RestController
@RequestMapping("/v1/collect")
@RequiredArgsConstructor
@Tag(name = "文章收藏API-V1")
public class ApiCollectController {

    private final ApiCollectService apiCollectService;

    @SaCheckLogin
    @GetMapping(value = "/")
    @Operation(description = "我的收藏列表", summary = "我的收藏列表")
    public ResponseResult selectCollectList() {
        return apiCollectService.selectCollectList();
    }

    @SaCheckLogin
    @AccessLimit
    @GetMapping(value = "collect")
    @Operation(description = "收藏文章", summary = "收藏文章")
    public ResponseResult collect(Integer articleId) {
        return apiCollectService.collect(articleId);
    }

    @SaCheckLogin
    @AccessLimit
    @DeleteMapping(value = "/")
    @Operation(description = "取消收藏", summary = "取消收藏")
    public ResponseResult cancel(Integer articleId) {
        return apiCollectService.cancel(articleId);
    }
}
