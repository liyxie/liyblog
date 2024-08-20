package com.liy.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.liy.annotation.AccessLimit;
import com.liy.common.ResponseResult;
import com.liy.dto.user.LoginDTO;
import com.liy.service.LoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author blue
 * @description:
 * @date 2021/7/30 14:37
 */
@RestController
@Api(tags = "登录-接口")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;


    @AccessLimit
    @GetMapping("verify")
    @ApiOperation(value = "生成验证码")
    public ResponseResult verify() {
        return loginService.getCaptcha();
    }

    @ApiOperation(value = "登录")
    @PostMapping("login")
    public ResponseResult login(@Validated @RequestBody LoginDTO vo) {
        return loginService.login(vo);
    }

    @ApiOperation(value = "登出")
    @GetMapping("logout")
    public ResponseResult logout() {
        StpUtil.logout();
        return ResponseResult.success("退出成功");
    }
}
