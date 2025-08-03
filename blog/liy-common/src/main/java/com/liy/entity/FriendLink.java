package com.liy.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.liy.utils.DateUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 友情链接表
 * </p>
 *
 * @author blue
 * @since 2021-08-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(title="FriendLink对象", description="友情链接表")
@TableName("b_friend_link")
public class FriendLink implements Serializable {

    private static final long serialVersionUID=1L;

    @Schema(description = "主键ID")
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "网站名称")
    private String name;

    @Schema(description = "网站地址")
    private String url;

    @Schema(description = "网站头像地址")
    private String avatar;

    @Schema(description = "网站描述")
    private String info;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "状态( 0,下架;1,申请;2:上架)")
    private Integer status;

    @Schema(description = "下架原因")
    private String reason;

    @Schema(description = "创建时间")
      @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = DateUtil.FORMAT_STRING,timezone="GMT+8")
    private Date createTime;

    @Schema(description = "修改时间")
      @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = DateUtil.FORMAT_STRING,timezone="GMT+8")
    private Date updateTime;


}
