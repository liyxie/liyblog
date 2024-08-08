package com.liy.config.satoken;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.liy.utils.DateUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * author: quequnlong
 * created: 2024/5/16 8:58
 * description:
 */
@Data
@Builder
public class OnlineUser {
    @ApiModelProperty("登录账号的")
    private String loginId;
    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("昵称")

    private String nickname;
    @ApiModelProperty("头像")

    private String avatar;
    @ApiModelProperty("ip地址")

    private String ip;
    @ApiModelProperty("系统")

    private String os;
    @ApiModelProperty("登录地址")

    private String city;
    @ApiModelProperty("浏览器")

    private String browser;
    @ApiModelProperty("token")
    private String tokenValue;
    /**
     * 登录时间
     */
    @ApiModelProperty("登录时间")
    @JsonFormat(pattern = DateUtil.FORMAT_STRING, timezone = "GMT+8")
    private Date loginTime;
    /**
     * 最近一次操作时间
     */
    @ApiModelProperty("最近一次操作时间")
    @JsonFormat(pattern = DateUtil.FORMAT_STRING, timezone = "GMT+8")
    private Date lastActivityTime;
}

