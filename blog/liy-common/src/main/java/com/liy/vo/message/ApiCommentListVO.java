package com.liy.vo.message;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiCommentListVO {
    @Schema(description = "主键id")
    private Integer id;
    @Schema(description = "发表用户id")
    private String userId;
    @Schema(description = "评论用户id")
    private String replyUserId;

    @Schema(description = "评论内容")
    private String content;

    @Schema(description = "发表用户昵称")
    private String nickname;
    @Schema(description = "用户站点")
    private String webSite;

    @Schema(description = "回复用户昵称")
    private String replyNickname;

    @Schema(description = "回复用户站点")
    private String replyWebSite;

    @Schema(description = "发表用户头像")
    private String avatar;

    @Schema(description = "发表时间")
    private Date createTime;

    @Schema(description = "发表时间")
    private String createTimeStr;

    @Schema(description = "浏览器")
    private String browser;

    @Schema(description = "浏览器版本")
    private String browserVersion;

    @Schema(description = "电脑系统")
    private String system;

    @Schema(description = "系统版本")
    private String systemVersion;

    @Schema(description = "ip地址")
    private String ipAddress;

    @Schema(description = "子评论集合")
    List<ApiCommentListVO> children = new ArrayList<>();


}
