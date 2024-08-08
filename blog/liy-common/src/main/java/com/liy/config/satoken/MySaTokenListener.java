package com.liy.config.satoken;

import cn.dev33.satoken.listener.SaTokenListener;
import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.json.JSONUtil;
import com.liy.common.RedisConstants;
import com.liy.entity.User;
import com.liy.mapper.UserMapper;
import com.liy.service.RedisService;
import com.liy.utils.DateUtil;
import com.liy.utils.IpUtil;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * author: quequnlong
 * created: 2024/5/16 8:57
 * description: 自定义侦听器的实现
 */
@Component
@RequiredArgsConstructor
public class MySaTokenListener implements SaTokenListener {

    private static final Logger logger = LoggerFactory.getLogger(MySaTokenListener.class);

    private final UserMapper userMapper;

    private final RedisService redisService;

    @Value("${spring.sa-token.timeout}")
    private Integer timeout;

    /** 每次登录时触发 */
    @Override
    public void doLogin(String loginType, Object loginId, SaLoginModel loginModel) {
        //修改登录信息
        String ip = IpUtil.getIp();
        String cityInfo = IpUtil.getCityInfo(ip);
        UserAgent userAgent = IpUtil.getUserAgent(Objects.requireNonNull(IpUtil.getRequest()));
        userMapper.updateLoginInfo(loginId,ip,cityInfo,userAgent.getOperatingSystem().getName(),userAgent.getBrowser().getName());

        User user = userMapper.selectById(loginId.toString());
        //暂时使用内存方式存储在线用户信息
        String token = StpUtil.getTokenValue();

        OnlineUser build = OnlineUser.builder()
                .avatar(user.getAvatar())
                .ip(ip)
                .city(cityInfo)
                .loginTime(DateUtil.getNowDate())
                .os(userAgent.getOperatingSystem().getName())
                .userId(loginId.toString())
                .tokenValue(token)
                .nickname(userMapper.getById(loginId).getNickname())
                .browser(userAgent.getBrowser().getName()).build();
        redisService.setCacheObject(RedisConstants.LOGIN_TOKEN + token, JSONUtil.toJsonStr(build), timeout, TimeUnit.SECONDS);

        logger.info("用户已登录,useId:{},token:{}", loginId, token);
    }

    /** 每次注销时触发 */
    @Override
    public void doLogout(String loginType, Object loginId, String tokenValue) {
        redisService.deleteObject(RedisConstants.LOGIN_TOKEN + tokenValue);
        logger.info("用户已注销,useId:{},token:{}", loginId, tokenValue);
    }

    /** 每次被踢下线时触发 */
    @Override
    public void doLogoutByLoginId(String loginType, Object loginId, String tokenValue, String device) {
        redisService.deleteObject(RedisConstants.LOGIN_TOKEN + tokenValue);
        logger.info("用户已踢下线,useId:{},token:{}", loginId, tokenValue);
    }

    /** 每次被顶下线时触发 */
    @Override
    public void doReplaced(String loginType, Object loginId, String tokenValue, String device) {
        redisService.deleteObject(RedisConstants.LOGIN_TOKEN + tokenValue);
        logger.info("用户已顶下线,useId:{},token:{}", loginId, tokenValue);
    }


    /** 每次被封禁时触发 */
    @Override
    public void doDisable(String loginType, Object loginId, long disableTime) {
        // ...
    }

    /** 每次被解封时触发 */
    @Override
    public void doUntieDisable(String loginType, Object loginId) {
        // ...
    }

    /** 每次创建Session时触发 */
    @Override
    public void doCreateSession(String id) {
        // ...
    }

    /** 每次注销Session时触发 */
    @Override
    public void doLogoutSession(String id) {
        // ...
        logger.info("user doLogoutSession,id:{}", id);
    }

}
