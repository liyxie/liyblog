package com.liy.vo.article;

import com.liy.entity.Category;
import com.liy.entity.Tags;
import com.liy.vo.message.ApiCommentListVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ApiArticleInfoVO {

    @Schema(description = "主键id")
    private Long id;

    @Schema(description = "用户id")
    private String userId;

    @Schema(description = "所属用户名")
    private String nickname;

    @Schema(description = "文章标题")
    private String title;

    @Schema(description = "作者头像")
    private String avatar;

    @Schema(description = "文章简介")
    private String summary;
    @Schema(description = "文章内容")
    private String content;

    @Schema(description = "文章内容MD版")
    private String contentMd;

    @Schema(description = "是否置顶 0否 1是")
    private Integer isStick;

    @Schema(description = "发布状态")
    private Integer isPublish;

    @Schema(description = "是否原创  0：转载 1:原创")
    private Integer isOriginal;

    @Schema(description = "转载地址")
    private String originalUrl;

    @Schema(description = "关键词")
    private String keywords;

    @Schema(description = "发布地址")
    private String address;

    @Schema(description = "阅读方式 0无需验证 1：评论阅读 2：点赞阅读 3：扫码阅读")
    private Integer readType;

    @Schema(description = "文章阅读量")
    private Integer quantity;


    @Schema(description = "点赞量")
    private Object likeCount;

    @Schema(description = "评论量")
    private Integer commentCount;

    @Schema(description = "发表时间")
    private Date createTime;

    @Schema(description = "修改时间")
    private Date updateTime;

    @Schema(description = "标签集合")
    private List<Tags> tagList;

    @Schema(description = "分类")
    private Category category;

    @Schema(description = "当前用户是否点赞")
    private Boolean isLike = false;

    @Schema(description = "当前用户是否收藏")
    private int isCollect;

    @Schema(description = "当前用户是否关注作者")
    private int isFollowed;

    @Schema(description = "收藏量")
    private int collectCount;

    @Schema(description = "当前用户激活阅读方式 如已评论或已点赞或已扫码验证过")
    private Boolean activeReadType = false;

    @Schema(description = "评论")
    private List<ApiCommentListVO> apiCommentListVos;


}
