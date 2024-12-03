package com.liy.service.impl;

import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liy.dto.WechatAppletDTO;
import com.liy.entity.Article;
import com.liy.entity.Collect;
import com.liy.entity.Followed;
import com.liy.entity.User;
import com.liy.mapper.ArticleMapper;
import com.liy.mapper.CollectMapper;
import com.liy.mapper.FollowedMapper;
import com.liy.mapper.UserMapper;
import com.liy.service.ApiUserService;
import com.liy.common.Constants;
import com.liy.common.RedisConstants;
import com.liy.common.ResponseResult;
import com.liy.common.ResultCode;
import com.liy.utils.*;

import com.liy.dto.user.EmailForgetPasswordDTO;
import com.liy.dto.user.EmailLoginDTO;
import com.liy.dto.user.EmailRegisterDTO;
import com.liy.dto.user.UserInfoDTO;

import com.liy.enums.LoginTypeEnum;
import com.liy.enums.UserStatusEnum;
import com.liy.exception.BusinessException;

import com.liy.service.EmailService;
import com.liy.service.RedisService;

import com.liy.vo.user.SystemUserVO;
import com.liy.vo.user.UserInfoVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.zhyd.oauth.model.AuthResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import static com.liy.common.ResultCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApiUserServiceImpl implements ApiUserService {

    private final UserMapper userMapper;

    private final ArticleMapper articleMapper;

    private final CollectMapper collectMapper;

    private final RedisService redisService;

    private final FollowedMapper followedMapper;

    private final EmailService emailService;

    @Value("${httpRedirect}")
    private String redirect;

    private final ObjectMapper objectMapper = new ObjectMapper();


    private final String[] userAvatarList = {"http://img.shiyit.com/avatars/buxie.png","http://img.shiyit.com/avatars/daizhi.png",
            "http://img.shiyit.com/avatars/fennu.png","http://img.shiyit.com/avatars/jingxi.png","http://img.shiyit.com/avatars/kaixin.png",
            "http://img.shiyit.com/avatars/shuanshuai.png"};


    /**
     * 邮箱登录
     *
     * @param vo
     * @return
     */
    @Override
    public ResponseResult emailLogin(EmailLoginDTO vo) {

        User user = userMapper.selectNameAndPassword(vo.getEmail(), AesEncryptUtil.aesEncrypt(vo.getPassword()));
        if (user == null) {
            throw new BusinessException(ERROR_PASSWORD.desc);
        }

        if (user.getStatus() == UserStatusEnum.disable.code) {
            throw new BusinessException(DISABLE_ACCOUNT.desc);
        }

        //七天有效时间  登录
        StpUtil.setLoginId(user.getId(), new SaLoginModel().setDevice("PC").setTimeout(60 * 60 * 24 * 7));

        //组装数据
        UserInfoVO userInfoVO = BeanCopyUtil.copyObject(user, UserInfoVO.class);
        userInfoVO.setToken(StpUtil.getTokenValue());

        StpUtil.getSession().set(Constants.CURRENT_USER,userMapper.getById(user.getId()));

        return ResponseResult.success(userInfoVO);
    }

    /**
     * 判断用户是否微信登录成功
     *
     * @param loginCode 用户临时id
     * @return
     */
    @Override
    public ResponseResult wxIsLogin(String loginCode) {
        Object value =redisService.getCacheObject(RedisConstants.WX_LOGIN_USER + loginCode);

        if (value == null) {
            return ResponseResult.error("用户未被授权");
        }
        UserInfoVO user = objectMapper.convertValue(value, UserInfoVO.class);
        StpUtil.setLoginId(user.getId(), new SaLoginModel().setDevice("PC").setTimeout(60 * 60 * 24 * 7));
        user.setToken(StpUtil.getTokenValue());
        return ResponseResult.success(user);
    }

    /**
     * 微信扫码公众号登录
     * @param message
     * @return
     */
    @Override
    public String wechatLogin(WxMpXmlMessage message) {
        String content = message.getContent().toUpperCase();
        //先判断登录码是否已过期
        boolean loginFlag = redisService.hasKey(RedisConstants.WX_LOGIN_USER_STATUE + content);
        if (!loginFlag) {
            return "验证码已过期";
        }
        UserInfoVO userInfoVO = wechatLogin(message.getFromUser());


        //修改redis缓存 以便监听是否已经授权成功
        redisService.setCacheObject(RedisConstants.WX_LOGIN_USER + content,userInfoVO, 60, TimeUnit.SECONDS);
        return "网站登录成功！(若页面长时间未跳转请刷新验证码)";
    }



    /**
     * 获取微信公众号登录验证码
     *
     * @return
     */
    @Override
    public ResponseResult getWechatLoginCode() {
        String code = "DL" + RandomUtil.generationNumberChar(4);
        redisService.setCacheObject(RedisConstants.WX_LOGIN_USER_STATUE + code, false, 60, TimeUnit.SECONDS);
        return ResponseResult.success(code);
    }

    /**
     * 获取用户信息
     *
     * @return
     */
    @Override
    public ResponseResult selectUserInfo(String userId) {
        userId = StringUtils.isNotBlank(userId) ? userId : StpUtil.getLoginIdAsString();
        UserInfoVO userInfo = userMapper.selectInfoByUserId(userId);
        return ResponseResult.success(userInfo);
    }

    public SystemUserVO getUser(String userId) {
        userId = StringUtils.isNotBlank(userId) ? userId : StpUtil.getLoginIdAsString();
        SystemUserVO userInfo = userMapper.getById(userId);
        return userInfo;
    }

    /**
     * 修改用户信息
     *
     * @param vo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult updateUser(UserInfoDTO vo) {
        User user = userMapper.selectById(StpUtil.getLoginIdAsString());
        if (ObjectUtils.isEmpty(user)) {
            throw  new BusinessException("用户不存在");
        }
        user = BeanCopyUtil.copyObject(vo, User.class);
        user.setId(StpUtil.getLoginIdAsString());
        userMapper.updateById(user);

        return ResponseResult.success();
    }

    /**
     * 根据token获取用户信息
     *
     * @param token
     * @return
     */
    @Override
    public ResponseResult selectUserInfoByToken(String token) {
        Object userId = StpUtil.getLoginIdByToken(token);
        UserInfoVO userInfoVO = userMapper.selectInfoByUserId(userId);
        return ResponseResult.success(userInfoVO);
    }

    /**
     * 第三方登录授权之后的逻辑
     *
     * @param response
     * @param source
     * @param httpServletResponse
     * @throws IOException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void authLogin(AuthResponse response, String source,  HttpServletResponse httpServletResponse) throws IOException {
        if (response.getData() == null) {
            log.info("用户取消了 {} 第三方登录",source);
            httpServletResponse.sendRedirect(redirect);
            return;
        }
        String result = com.alibaba.fastjson.JSONObject.toJSONString(response.getData());
        log.info("第三方登录验证结果:{}", result);

        com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(result);
        Object uuid = jsonObject.get("uuid");
        // 获取用户ip信息
        String ipAddress = IpUtil.getIp();
        String ipSource = IpUtil.getIp2region(ipAddress);
        // 判断是否已注册
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, uuid));
        if (Objects.nonNull(user)) {
            // 更新登录信息
            userMapper.update(new User(), new LambdaUpdateWrapper<User>()
                    .set(User::getLastLoginTime, LocalDateTime.now())
                    .set(User::getIpAddress, ipAddress)
                    .set(User::getIpSource, ipSource)
                    .eq(User::getId, user.getId()));
        } else {
            // 保存账号信息
            user = User.builder()
                    .username(uuid.toString())
                    .password(UUID.randomUUID().toString())
                    .loginType(LoginTypeEnum.getType(source))
                    .lastLoginTime(DateUtil.getNowDate())
                    .ipAddress(ipAddress)
                    .ipSource(ipSource)
                    .roleId(2)
                    .status(UserStatusEnum.normal.getCode())
                    .nickname(source.equals("github") ? jsonObject.get("username").toString() : jsonObject.get("nickname").toString())
                    .avatar(jsonObject.get("avatar").toString())
                    .build();
            userMapper.insert(user);
        }

        StpUtil.setLoginId(user.getId(), new SaLoginModel().setDevice("PC").setTimeout(60 * 60 * 24 * 7));
        httpServletResponse.sendRedirect(redirect + "/?token=" + StpUtil.getTokenValue());
    }

    @Override
    public ResponseResult sendEmailCode(String email) {
        try {
            if (!emailRegex(email)) {
                throw new BusinessException(ResultCode.EMAIL_PATTERN_ERROR);
            }
            emailService.sendCode(email);
            return ResponseResult.success();
        } catch (MessagingException e) {
            throw new BusinessException("发送邮件失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult emailRegister(EmailRegisterDTO emailRegisterDTO) {
        //校验邮箱验证码
        boolean b = redisService.hasKey(RedisConstants.EMAIL_CODE + emailRegisterDTO.getEmail());
        if (!b) {
            throw new BusinessException(ResultCode.ERROR_EXCEPTION_MOBILE_CODE);
        }
        Integer count = userMapper.selectCount(new LambdaQueryWrapper<User>().eq(User::getUsername, emailRegisterDTO.getEmail()));
        if (count > 0) {
            throw new BusinessException("该邮箱账号已经注册");
        }
        // 保存账号信息
        User user = User.builder()
                .username(emailRegisterDTO.getEmail())
                .password(AesEncryptUtil.aesEncrypt(emailRegisterDTO.getPassword()))
                .loginType(LoginTypeEnum.getType("email"))
                .roleId(2)
                .status(UserStatusEnum.normal.getCode())
                .nickname(emailRegisterDTO.getNickname())
                .build();
        userMapper.insert(user);
        return ResponseResult.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult forgetPassword(EmailForgetPasswordDTO emailForgetPasswordDTO) {
        //校验邮箱验证码
        boolean b = redisService.hasKey(RedisConstants.EMAIL_CODE + emailForgetPasswordDTO.getEmail());
        if (!b) {
            throw new BusinessException(ResultCode.ERROR_EXCEPTION_MOBILE_CODE);
        }
        User user = User.builder().password(AesEncryptUtil.aesEncrypt(emailForgetPasswordDTO.getPassword())).build();
        userMapper.update(user,new LambdaQueryWrapper<User>().eq(User::getUsername,emailForgetPasswordDTO.getEmail()));
        return ResponseResult.success();
    }

    @Override
    public ResponseResult getUserCount(String id) {
        id = StringUtils.isBlank(id) ? StpUtil.getLoginIdAsString() : id;
        Integer articleCount = articleMapper.selectCount(new LambdaQueryWrapper<Article>().eq(Article::getUserId, id));
        Integer collectCount = collectMapper.selectCount(new LambdaQueryWrapper<Collect>().eq(Collect::getUserId, id));
        Integer followedCount = followedMapper.selectCount(new LambdaQueryWrapper<Followed>().eq(Followed::getUserId, id));
        return ResponseResult.success().putExtra("articleCount", articleCount).putExtra("collectCount", collectCount)
                .putExtra("followedCount", followedCount);
    }

    @Override
    public ResponseResult appletLogin(WechatAppletDTO wechatAppletDTO) {

        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=wx3e9678e6cdabd38f&secret=f8f33e962ab232ab70fd6545b86ac731&js_code="+wechatAppletDTO.getCode()+"&grant_type=authorization_code";
        String result = HttpUtil.get(url);

        JSONObject jsonObject = JSON.parseObject(result);
        String openid = jsonObject.get("openid").toString();
        UserInfoVO userInfoVO = this.wechatLogin(openid);

        StpUtil.setLoginId(userInfoVO.getId(), new SaLoginModel().setDevice("PC").setTimeout(60 * 60 * 24 * 7));
        userInfoVO.setToken(StpUtil.getTokenValue());
        return ResponseResult.success(userInfoVO);
    }



    //--------------------自定义方法分割线--------------------------

    private UserInfoVO wechatLogin(String openId) {
        UserInfoVO userInfo = userMapper.selectByUserName(openId);
        if (ObjectUtils.isEmpty(userInfo)) {
            String ip = IpUtil.getIp();
            String ipSource = IpUtil.getIp2region(ip);

            // 保存账号信息
            User user = User.builder()
                    .username(openId)
                    .password(AesEncryptUtil.aesEncrypt(openId))
                    .nickname("WECHAT-" + RandomUtil.generationCapital(6))
                    .avatar(userAvatarList[RandomUtil.generationNumber(userAvatarList.length)])
                    .loginType(LoginTypeEnum.WECHAT.getType())
                    .lastLoginTime(DateUtil.getNowDate())
                    .ipAddress(ip)
                    .ipSource(ipSource)
                    .roleId(2)
                    .build();
            userMapper.insert(user);
            userInfo = BeanCopyUtil.copyObject(user,UserInfoVO.class);
        }
        return userInfo;
    }


    public boolean emailRegex(String email) {
        Pattern pattern = Pattern.compile("^[A-Za-z0-9\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(.[a-zA-Z0-9_-]+)+$");
        return pattern.matcher(email).matches();
    }
}
