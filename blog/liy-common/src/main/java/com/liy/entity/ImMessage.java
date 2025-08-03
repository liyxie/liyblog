package com.liy.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.liy.utils.DateUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 博客评论表
 * </p>
 *
 * @author blue
 * @since 2021-07-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("b_im_message")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(title="ImMessage对象", description="群聊实体类")
public class ImMessage implements Serializable {

    private static final long serialVersionUID=1L;

    @Schema(description = "主键id")
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    @Schema(description = "发送用户Id")
    private String toUserId;

    @Schema(description = "接收用户id")
    private String fromUserId;

    @Schema(description = "发送用户头像")
    private String toUserAvatar;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "ip")
    private String ip;

    @Schema(description = "ip地址")
    private String ipSource;

    @Schema(description = "是否撤回")
    private int isWithdraw;

    @Schema(description = "评论标记 1回复评论 2发表评论")
    private Integer commentMark;

    @Schema(description = "消息类型 1私聊 2群聊")
    private Integer code;

    @Schema(description = "消息是否已读 0未读 2已读")
    private Integer isRead;

    @Schema(description = "消息内容类型 1普通消息 2图片")
    private Integer type;

    @Schema(description = "通知类型 0系统通知 1：评论 2：关注 3点赞 4收藏 5私信")
    private Integer noticeType;

    @Schema(description = "文章id")
    private Integer articleId;

    @Schema(description = "@用户id 多个逗号分隔")
    private String atUserId;

    @Schema(description = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = DateUtil.FORMAT_STRING,timezone="GMT+8")
    private Date createTime;





}
