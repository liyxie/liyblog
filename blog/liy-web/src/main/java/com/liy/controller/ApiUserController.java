package com.liy.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.liy.annotation.BusinessLogger;
import com.liy.common.ResponseResult;
import com.liy.dto.user.UserInfoDTO;
import com.liy.service.ApiUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/v1/user")
@RestController
@Api(tags = "登录接口")
@RequiredArgsConstructor
public class ApiUserController {

    private final ApiUserService userService;

    @SaCheckLogin
    @BusinessLogger(value = "个人中心模块-获取用户信息",type = "修改",desc = "获取用户信息")
    @RequestMapping(value = "/info",method = RequestMethod.GET)
    public ResponseResult selectUserInfo(String userId){
        return userService.selectUserInfo(userId);
    }

    @SaCheckLogin
    @BusinessLogger(value = "个人中心模块-修改用户信息",type = "修改",desc = "修改用户信息")
    @RequestMapping(value = "/",method = RequestMethod.PUT)
    public ResponseResult updateUser(@RequestBody UserInfoDTO vo){
        return userService.updateUser(vo);
    }

    @RequestMapping(value = "selectUserInfoByToken",method = RequestMethod.GET)
    @ApiOperation(value = "根据token获取用户信息", httpMethod = "GET", response = ResponseResult.class, notes = "根据token获取用户信息")
    public ResponseResult selectUserInfoByToken(String token){
        return userService.selectUserInfoByToken(token);
    }

    @RequestMapping(value = "getUserCount",method = RequestMethod.GET)
    @ApiOperation(value = "根据用户id统计用户文章、关注、粉丝等信息", httpMethod = "GET", response = ResponseResult.class, notes = "根据用户id统计用户文章、关注、粉丝等信息")
    public ResponseResult getUserCount(String id){
        return userService.getUserCount(id);
    }

}

