package com.liy.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import com.liy.annotation.OperationLogger;
import com.liy.common.ResponseResult;
import com.liy.entity.FeedBack;
import com.liy.service.FeedBackService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author blue
 * @since 2022-01-13
 */
@RestController
@RequestMapping("/system/feedback")
@Api(tags = "后台反馈管理")
@RequiredArgsConstructor
public class FeedBackController {

    private final FeedBackService feedBackService;

    @GetMapping(value = "/list")
    @ApiOperation(value = "反馈列表", httpMethod = "GET", response = ResponseResult.class, notes = "反馈列表")
    public ResponseResult selectFeedBackPage(Integer type) {
        return feedBackService.selectFeedBackPage(type);
    }

    @DeleteMapping(value = "/delete")
    @SaCheckPermission("system:feedback:delete")
    @ApiOperation(value = "删除反馈", httpMethod = "DELETE", response = ResponseResult.class, notes = "删除反馈")
    @OperationLogger(value = "删除反馈")
    public ResponseResult deleteFeedBack(@RequestBody List<Integer> ids) {
        return feedBackService.deleteFeedBack(ids);
    }

    @PutMapping(value = "/update")
    @OperationLogger(value = "修改反馈")
    @SaCheckPermission("system:feedback:update")
    @ApiOperation(value = "修改反馈", httpMethod = "PUT", response = ResponseResult.class, notes = "修改反馈")
    public ResponseResult update(@RequestBody FeedBack feedBack) {
        return feedBackService.updateFeedBack(feedBack);
    }
}

