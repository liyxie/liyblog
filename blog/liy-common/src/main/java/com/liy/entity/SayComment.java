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
@TableName("b_say_comment")
@Schema(title="SayComment", description="")
public class SayComment implements Serializable {
    private static final long serialVersionUID=1L;


    @Schema(description = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    @Schema(description = "用户Id")
    private String userId;

    @Schema(description = "回复用户Id")
    private String replyUserId;

    @Schema(description = "说说id")
    private String sayId;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "ip")
    private String ip;

    @Schema(description = "ip来源")
    private String ipAddress;

    @Schema(description = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = DateUtil.FORMAT_STRING,timezone="GMT+8")
    private Date createTime;

}
