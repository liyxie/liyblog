package com.liy.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.liy.utils.DateUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("b_say")
@Schema(title="Say", description="")
public class Say implements Serializable {
    private static final long serialVersionUID=1L;


    @Schema(description = "主键id")
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;


    @Schema(description = "用户Id")
    private String userId;


    @Schema(description = "图片地址 逗号分隔  最多九张")
    private String imgUrl;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "发表地址。可输入或者地图插件选择")
    private String address;

    @Schema(description = "是否开放查看  0未开放 1开放")
    private Integer isPublic;


    @Schema(description = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = DateUtil.FORMAT_STRING,timezone="GMT+8")
    private Date createTime;

    @Schema(description = "修改时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;
}
