package com.liy.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.liy.annotation.AccessLimit;
import com.liy.common.ResponseResult;
import com.liy.service.ApiFollowedService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/v1/followed")
@RequiredArgsConstructor
@Tag(name = "关注API-V1")
public class ApiFollowedController {

    private final ApiFollowedService followedService;

    @AccessLimit
    @SaCheckLogin
    @PostMapping(value = "/insertFollowed")
    @Operation(description = "关注用户", summary = "关注用户")
    public ResponseResult addFollowedUser(String userId) {
        return  followedService.addFollowedUser(userId);
    }

    @AccessLimit
    @SaCheckLogin
    @DeleteMapping(value = "/deleteFollowed")
    @Operation(description = "取消关注用户", summary = "取消关注用户")
    public ResponseResult deleteFollowed(String userId) {
        return  followedService.deleteFollowed(userId);
    }
}
