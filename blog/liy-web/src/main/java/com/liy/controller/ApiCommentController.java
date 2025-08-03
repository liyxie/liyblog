package com.liy.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.liy.annotation.AccessLimit;
import com.liy.annotation.BusinessLogger;
import com.liy.common.ResponseResult;
import com.liy.entity.Comment;
import com.liy.service.ApiCommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "评论API-V1")
@RestController
@RequestMapping("/v1/comment")
@RequiredArgsConstructor
public class ApiCommentController {

    private final ApiCommentService commentService;

    @AccessLimit
    @SaCheckLogin
    @BusinessLogger(value = "评论模块-用户评论",type = "添加",desc = "用户评论")
    @RequestMapping(value = "/",method = RequestMethod.POST)
    @Operation(description = "添加评论", summary = "添加评论")
    public ResponseResult addComment(@RequestBody Comment comment){
        return commentService.addComment(comment);
    }

    @RequestMapping(value = "/",method = RequestMethod.GET)
    @Operation(description = "根据文章id获取相关评论", summary = "根据文章id获取相关评论")
    public ResponseResult selectCommentByArticleId(Long articleId){
        return commentService.selectCommentByArticleId(articleId);
    }

    @RequestMapping(value = "/getMyComment",method = RequestMethod.GET)
    @Operation(description = "获取我的评论", summary = "获取我的评论")
    public ResponseResult selectMyComment(){
        return commentService.selectMyComment();
    }
}
