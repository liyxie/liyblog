package com.liy.controller;

import com.liy.annotation.AccessLimit;
import com.liy.common.ResponseResult;
import com.liy.config.properties.GiteeConfigProperties;
import com.liy.config.properties.GithubConfigProperties;
import com.liy.config.properties.QqConfigProperties;
import com.liy.config.properties.WeiboConfigProperties;
import com.liy.dto.user.EmailForgetPasswordDTO;
import com.liy.dto.user.EmailLoginDTO;
import com.liy.dto.user.EmailRegisterDTO;
import com.liy.dto.WechatAppletDTO;
import com.liy.service.ApiUserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.exception.AuthException;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.request.*;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ApiJustAuthController {


    private final GiteeConfigProperties giteeConfigProperties;

    private final QqConfigProperties qqConfigProperties;

    private final WeiboConfigProperties weiboConfigProperties;

    private final GithubConfigProperties githubConfigProperties;

    private  final ApiUserService userService;


    /**
     * 通过JustAuth的AuthRequest拿到第三方的授权链接，并跳转到该链接页面
     *
     * @param response
     * @throws IOException
     */
    @RequestMapping("/render/{source}")
    @Operation(description = "通过JustAuth的AuthRequest拿到第三方的授权链接，并跳转到该链接页面", summary = "获取第三方授权链接")
    public ResponseResult renderAuth(HttpServletResponse response, @PathVariable String source) throws IOException {
        AuthRequest authRequest = getAuthRequest(source);
        String authorizeUrl = authRequest.authorize(AuthStateUtils.createState());
        return ResponseResult.success(authorizeUrl);
    }

    /**
     * 用户在确认第三方平台授权（登录）后， 第三方平台会重定向到该地址，并携带code、state等参数
     * authRequest.login通过code向第三方请求用户数据
     *
     * @param callback 第三方回调时的入参
     * @return 第三方平台的用户信息
     */
    @RequestMapping("/callback/{source}")
    @Operation(description = "用户在确认第三方平台授权（登录）后， 第三方平台会重定向到该地址，并携带code、state等参数", summary = "第三方登录回调")
    public void login(AuthCallback callback, @PathVariable String source,  HttpServletResponse httpServletResponse) throws IOException {
        AuthRequest authRequest = getAuthRequest(source);
        AuthResponse response = authRequest.login(callback);
        userService.authLogin(response,source,httpServletResponse);
    }

    @AccessLimit
    @RequestMapping(value = "/emailLogin",method = RequestMethod.POST)
    @Operation(description = "账号密码登录", summary = "账号密码登录")
    public ResponseResult emailLogin(@Valid @RequestBody EmailLoginDTO emailLoginDTO){
        return userService.emailLogin(emailLoginDTO);
    }

    @Operation(description = "判断用户是否微信登录成功", summary = "判断用户是否微信登录成功")
    @RequestMapping("/wechat/is_login")
    public ResponseResult wxIsLogin( String loginCode) {
        return userService.wxIsLogin(loginCode);
    }

    @Operation(description = "获取微信登录验证码", summary = "获取微信登录验证码")
    @RequestMapping("/wechatLoginCode")
    public ResponseResult getWechatLoginCode() {
        return userService.getWechatLoginCode();
    }

    @Operation(description = "发送邮箱验证码", summary = "发送邮箱验证码")
    @RequestMapping("/sendEmailCode")
    public ResponseResult sendEmailCode(String email) {
        return userService.sendEmailCode(email);
    }

    @AccessLimit
    @RequestMapping(value = "/emailRegister",method = RequestMethod.POST)
    @Operation(description = "邮箱注册", summary = "邮箱注册")
    public ResponseResult emailRegister(@Valid @RequestBody EmailRegisterDTO emailRegisterDTO){
        return userService.emailRegister(emailRegisterDTO);
    }

    @AccessLimit
    @RequestMapping(value = "/forgetPassword",method = RequestMethod.PUT)
    @Operation(description = "忘记密码", summary = "忘记密码")
    public ResponseResult forgetPassword(@Valid @RequestBody EmailForgetPasswordDTO emailForgetPasswordDTO){
        return userService.forgetPassword(emailForgetPasswordDTO);
    }

    @RequestMapping(value = "/applet",method = RequestMethod.POST)
    @Operation(description = "小程序登录", summary = "小程序登录")
    public ResponseResult appletLogin(@RequestBody WechatAppletDTO wechatAppletDTO){
        return userService.appletLogin(wechatAppletDTO);
    }

    /**
     * 创建授权request
     *
     * @return AuthRequest
     */
    private AuthRequest getAuthRequest(String source) {
        AuthRequest authRequest = null;
        switch (source) {
            case "gitee":
                authRequest = new AuthGiteeRequest(AuthConfig.builder()
                        .clientId(giteeConfigProperties.getAppId())
                        .clientSecret(giteeConfigProperties.getAppSecret())
                        .redirectUri(giteeConfigProperties.getRedirectUrl())
                        .build());
                break;
            case "qq":
                authRequest = new AuthQqRequest(AuthConfig.builder()
                        .clientId(qqConfigProperties.getAppId())
                        .clientSecret(qqConfigProperties.getAppSecret())
                        .redirectUri(qqConfigProperties.getRedirectUrl())
                        .build());
                break;
            case "weibo":
                authRequest = new AuthWeiboRequest(AuthConfig.builder()
                        .clientId(weiboConfigProperties.getAppId())
                        .clientSecret(weiboConfigProperties.getAppSecret())
                        .redirectUri(weiboConfigProperties.getRedirectUrl())
                        .build());
                break;
            case "github":
                authRequest = new AuthGithubRequest(AuthConfig.builder()
                        .clientId(githubConfigProperties.getAppId())
                        .clientSecret(githubConfigProperties.getAppSecret())
                        .redirectUri(githubConfigProperties.getRedirectUrl())
                        .build());
                break;
            default:
                break;
        }
        if (null == authRequest) {
            throw new AuthException("授权地址无效");
        }
        return authRequest;
    }

}