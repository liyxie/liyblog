package com.liy.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("b_followed")
@Schema(title="Followed", description="")
public class Followed implements Serializable {
    private static final long serialVersionUID=1L;


    @Schema(description = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    @Schema(description = "用户Id")
    private String userId;


    @Schema(description = "关注的用户id")
    private String followedUserId;


    @Schema(description = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
}
