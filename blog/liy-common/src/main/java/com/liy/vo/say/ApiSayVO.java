package com.liy.vo.say;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.liy.utils.DateUtil;
import com.liy.vo.user.UserInfoVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ApiSayVO {
    @Schema(description = "主键id")
    private String id;


    @Schema(description = "用户Id")
    private String userId;

    @Schema(description = "用户昵称")
    private String nickname;

    @Schema(description = "用户头像")
    private String avatar;


    @Schema(description = "图片地址 逗号分隔  最多九张")
    private String imgUrl;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "发表地址。可输入或者地图插件选择")
    private String address;

    @Schema(description = "是否开放查看  0未开放 1开放")
    private Integer isPublic;

    @Schema(description = "点赞用户")
    private List<UserInfoVO> userLikeList;

    @Schema(description = "是否点赞")
    private Boolean isLike;

    @Schema(description = "评论集合")
    private List<ApiSayCommentVO> sayCommentVOList;


    @Schema(description = "创建时间")
    @JsonFormat(pattern = DateUtil.FORMAT_STRING,timezone="GMT+8")
    private Date createTime;

    @Schema(description = "创建时间")
    private String createTimeStr;
}
