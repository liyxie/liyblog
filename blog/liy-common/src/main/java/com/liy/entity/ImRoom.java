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
@TableName("b_im_room")
@Schema(title="ImRoom", description="")
public class ImRoom implements Serializable {
    private static final long serialVersionUID=1L;


    @Schema(description = "主键id")
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;


    @Schema(description = "房间类型 0：群聊 1私聊")
    private Integer type;

    @Schema(description = "发送用户id")
    private String fromUserId;

    @Schema(description = "接收用户id")
    private String toUserId;


    @Schema(description = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = DateUtil.FORMAT_STRING,timezone="GMT+8")
    private Date createTime;
}
