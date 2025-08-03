package com.liy.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.liy.common.ResponseResult;
import com.liy.entity.FeedBack;
import com.liy.service.ApiFeedBackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author blue
 * @date 2022/1/13
 * @apiNote
 */
@RestController
@RequestMapping("/v1/feedback")
@RequiredArgsConstructor
@Tag(name = "反馈API-V1")
public class ApiFeedBackController {

    private final ApiFeedBackService feedBackService;

    @SaCheckLogin
    @PostMapping(value = "/")
    @Operation(description = "添加反馈", summary = "添加反馈")
    public ResponseResult addFeedback(@RequestBody FeedBack feedBack) {
        return feedBackService.addFeedback(feedBack);
    }

}
