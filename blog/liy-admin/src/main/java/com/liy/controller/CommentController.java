package com.liy.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.liy.annotation.OperationLogger;
import com.liy.common.ResponseResult;
import com.liy.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/system/comment")
@Tag(name = "评论管理")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @Operation(summary = "评论列表")
    public ResponseResult selectCommentPage(String keywords){
        return commentService.selectCommentPage(keywords);
    }

    @RequestMapping(value = "/delete",method = RequestMethod.DELETE)
    @SaCheckPermission("system:comment:delete")
    @Operation(summary = "批量删除评论")
    @OperationLogger(value = "删除评论")
    public ResponseResult deleteBatch(@RequestBody List<Integer> ids){
        return commentService.deleteComment(ids);
    }

}
