package com.liy.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 系统管理-用户基础信息表
 * </p>
 *
 * @author blue
 * @since 2021-07-30
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@TableName("b_user")
@Schema(title="User对象", description="系统管理-用户基础信息表")
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {

    private static final long serialVersionUID=1L;

    @Schema(description = "主键ID")
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    @Schema(description = "账号")
    private String username;

    @Schema(description = "登录密码")
    private String password;

    @Schema(description = "状态")
    private Integer status;


    @Schema(description = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @Schema(description = "最后更新时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @Schema(description = "最后登录时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date lastLoginTime;

    @Schema(description = "角色ID")
    private Integer roleId;

    @Schema(description = "IP地址")
    private String ipAddress;

    @Schema(description = "IP来源")
    private String ipSource;

    @Schema(description = "登录系统")
    private String os;

    @Schema(description = "浏览器")
    private String browser;

    @Schema(description = "登录类型")
    private Integer loginType;

    @Schema(description = "用户昵称")
    private String nickname;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "个人简介")
    private String intro;

    @Schema(description = "个人站点")
    private String webSite;

    @Schema(description = "背景封面")
    private String bjCover;

}
