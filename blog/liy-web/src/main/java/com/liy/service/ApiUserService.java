package com.liy.service;

import com.liy.common.ResponseResult;
import com.liy.dto.WechatAppletDTO;
import com.liy.dto.user.EmailForgetPasswordDTO;
import com.liy.dto.user.EmailLoginDTO;
import com.liy.dto.user.EmailRegisterDTO;
import com.liy.dto.user.UserInfoDTO;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.zhyd.oauth.model.AuthResponse;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface ApiUserService {
    /**
     * 邮箱登录
     * @param emailLoginDTO
     * @return
     */
    public ResponseResult emailLogin(EmailLoginDTO emailLoginDTO);

    /**
     * 获取微信登录验证码
     * @return
     */
    public ResponseResult getWechatLoginCode();

    /**
     * 微信公众号登录
     * @param message
     * @return
     */
    public String wechatLogin(WxMpXmlMessage message);

    /**
     * 判断用户是否微信登录成功
     * @param loginCode 登录码
     * @return
     */
    public ResponseResult wxIsLogin(String loginCode);


    /**
     * 获取用户信息
     * @return
     */
    public ResponseResult selectUserInfo(String userId);

    /**
     * 修改用户信息
     * @param vo
     * @return
     */
    public ResponseResult updateUser(UserInfoDTO vo);

    /**
     * 根据token获取用户信息
     * @param token
     * @return
     */
    public ResponseResult selectUserInfoByToken(String token);


    /**
     * 第三方登录授权之后的逻辑
     * @param response
     * @param source
     * @param httpServletResponse
     * @throws IOException
     */
    public void authLogin(AuthResponse response, String source, HttpServletResponse httpServletResponse) throws IOException;

    /**
     * 发送邮箱验证码
     * @param email
     * @return
     */
    public ResponseResult sendEmailCode(String email);

    /**
     * 邮箱注册
     * @param emailRegisterDTO
     * @return
     */
    public ResponseResult emailRegister(EmailRegisterDTO emailRegisterDTO);

    /**
     * 忘记密码
     * @param emailForgetPasswordDTO
     * @return
     */
    public ResponseResult forgetPassword(EmailForgetPasswordDTO emailForgetPasswordDTO);

    /**
     * 根据用户id统计用户文章、关注、粉丝等信息
     * @param id
     * @return
     */
    public ResponseResult getUserCount(String id);


    /**
     * 微信小程序登录
     * @param wechatAppletDTO
     * @return
     */
    ResponseResult appletLogin(WechatAppletDTO wechatAppletDTO);

}
