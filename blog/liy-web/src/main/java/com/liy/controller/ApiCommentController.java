package com.liy.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.liy.annotation.AccessLimit;
import com.liy.annotation.BusinessLogger;
import com.liy.common.ResponseResult;
import com.liy.entity.Comment;
import com.liy.service.ApiCommentService;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/comment")
@Schema(title = "评论API-V1")
@RequiredArgsConstructor
public class ApiCommentController {

    private final ApiCommentService commentService;

    @AccessLimit
    @SaCheckLogin
    @BusinessLogger(value = "评论模块-用户评论",type = "添加",desc = "用户评论")
    @RequestMapping(value = "/",method = RequestMethod.POST)
    @Schema(description = "添加评论", httpMethod = "POST", response = ResponseResult.class, notes = "添加评论")
    public ResponseResult addComment(@RequestBody Comment comment){
        return commentService.addComment(comment);
    }

    @RequestMapping(value = "/",method = RequestMethod.GET)
    @Schema(description = "根据文章id获取相关评论", httpMethod = "GET", response = ResponseResult.class, notes = "根据文章id获取相关评论")
    public ResponseResult selectCommentByArticleId(Long articleId){
        return commentService.selectCommentByArticleId(articleId);
    }

    @RequestMapping(value = "/getMyComment",method = RequestMethod.GET)
    @Schema(description = "获取我的评论", httpMethod = "GET", response = ResponseResult.class, notes = "获取我的评论")
    public ResponseResult selectMyComment(){
        return commentService.selectMyComment();
    }
}
