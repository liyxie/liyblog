package com.liy.config.satoken;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "登录账号的")
    private String loginId;
    @Schema(description = "用户id")
    private String userId;

    @Schema(description = "昵称")
    private String nickname;
    
    @Schema(description = "头像")
    private String avatar;
    
    @Schema(description = "ip地址")
    private String ip;
    
    @Schema(description = "系统")
    private String os;
    
    @Schema(description = "登录地址")
    private String city;
    
    @Schema(description = "浏览器")
    private String browser;
    
    @Schema(description = "token")
    private String tokenValue;
    /**
     * 登录时间
     */
    @Schema(description = "登录时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date loginTime;
    /**
     * 最近一次操作时间
     */
    @Schema(description = "最近一次操作时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastActivityTime;
}

