package com.liy.vo.say;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.liy.utils.DateUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiSayCommentVO {
    @Schema(description = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    @Schema(description = "用户Id")
    private String userId;

    @Schema(description = "用户昵称")
    private String nickname;

    @Schema(description = "回复用户Id")
    private String replyUserId;

    @Schema(description = "回复用户昵称")
    private String replyUserNickname;

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

    @Schema(description = "创建时间")
    private String createTimeStr;


}
