package com.liy.vo.message;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.liy.utils.DateUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
public class ImMessageVO {

    @Schema(description = "主键id")
    private String id;

    @Schema(description = "消息类型")
    private Integer code;

    @Schema(description = "接收用户Id")
    private String toUserId;

    @Schema(description = "发送用户id")
    private String fromUserId;

    @Schema(description = "发送用户头像")
    private String fromUserAvatar;

    @Schema(description = "发送用户昵称")
    private String fromUserNickname;


    @Schema(description = "内容")
    private String content;

    @Schema(description = "ip")
    private String ip;

    @Schema(description = "ip地址")
    private String ipSource;

    @Schema(description = "是否撤回")
    private int isWithdraw;

    @Schema(description = "文章id")
    private Integer articleId;

    @Schema(description = "文章标题")
    private String articleTitle;

    @Schema(description = "消息类型 1普通消息 2图片")
    private int type;

    @Schema(description = "是否已读 0:未读 1：已读")
    private int isRead;

    @Schema(description = "选中下标")
    private Integer index;

    @Schema(description = "评论标记 1回复评论 2发表评论")
    private Integer commentMark;

    @Schema(description = "通知类型 0系统通知 1：评论 2：关注 3点赞 4收藏 5私信")
    private Integer noticeType;

    @Schema(description = "@用户id 多个逗号分隔")
    private String atUserId;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = DateUtil.YYYY_MM_DD_HH_MM,timezone="GMT+8")
    private Date createTime;

    @Schema(description = "创建时间")
    private String createTimeStr;

    @Schema(description = "扩展信息")
    private Map<String, Object> ext;
}
