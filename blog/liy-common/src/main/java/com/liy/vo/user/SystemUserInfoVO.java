package com.liy.vo.user;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.liy.utils.DateUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @author blue
 * @date 2022/3/7
 * @apiNote
 */
@Data
public class SystemUserInfoVO {

    private String id;

    @Schema(description = "状态")
    private Integer status;

    @Schema(description = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = DateUtil.FORMAT_STRING, timezone = "GMT+8")
    private Date createTime;

    @Schema(description = "最后更新时间")
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = DateUtil.FORMAT_STRING, timezone = "GMT+8")
    private Date updateTime;

    @Schema(description = "最后登录时间")
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = DateUtil.FORMAT_STRING, timezone = "GMT+8")
    private Date lastLoginTime;

    @Schema(description = "角色ID")
    private Integer roleId;

    @Schema(description = "IP地址")
    private String ipAddress;

    @Schema(description = "IP来源")
    private String ipSource;

    private Integer loginType;

    /**
     * 昵称
     * */
    private String nickname;

    /**
     * 头像
     * */
    private String avatar;
}
