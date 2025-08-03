package com.liy.vo.article;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.liy.entity.Tags;
import com.liy.utils.DateUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * 门户的文章列表vo多个地方使用到了 修改需谨慎
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiArticleListVO {

    @Schema(description = "主键id")
    private Long id;

    @Schema(description = "作者昵称")
    private String nickname;

    @Schema(description = "作者头像")
    private String userAvatar;

    @Schema(description = "作者id")
    private String userId;

    @Schema(description = "文章标题")
    private String title;

    @Schema(description = "文章封面图")
    private String avatar;

    @Schema(description = "文章描述")
    private String summary;

    @Schema(description = "文章内容")
    private String content;

    @Schema(description = "是否置顶")
    private Integer isStick;

    @Schema(description = "是否原创")
    private Integer isOriginal;

    @Schema(description = "是否发布")
    private Integer isPublish;

    @Schema(description = "阅读量")
    private Integer quantity;

    @Schema(description = "评论数")
    private Integer commentCount;

    @Schema(description = "点赞量")
    private Object likeCount;

    @Schema(description = "收藏量")
    private int collectCount;

    @Schema(description = "当前用户是否收藏")
    private Boolean isCollect;

    @Schema(description = "我的评论内容")
    private String commentContent;

    @Schema(description = "分类名")
    private String categoryName;

    @Schema(description = "分类id")
    private String categoryId;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = DateUtil.FORMAT_STRING,timezone="GMT+8")
    private Date createTime;

    @Schema(description = "格式化后的创建时间")
    private String formatCreateTime;

    @Schema(description = "标签集合")
    private List<Tags> tagList;
}
