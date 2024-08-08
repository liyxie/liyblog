package com.liy.dto;

import lombok.Data;

/**
 * @author: 拾壹
 * @date: 2023/11/9
 * @describe: 小程序微信授权登录dto
 */
@Data
public class WechatAppletDTO {

    /**
     *
     * @return 登录code
     */
    private String code;

    /**
     *
     * @return 用户头像
     */
    private String avatarUrl;
    /**
     *
     * @return 用户昵称
     */
    private String nickName;
}
