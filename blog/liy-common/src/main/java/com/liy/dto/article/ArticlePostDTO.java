package com.liy.dto.article;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class ArticlePostDTO {
    @Schema(name = "id", description = "文章id")
    private Long id;
    @Schema(name = "title", description = "文章标题")
    private String title;

    @Schema(name = "summary", description = "文章简介")
    private String summary;

    @Schema(name = "avatar", description = "文章封面")
    private String avatar;

    @Schema(name = "categoryId", description = "文章分类id")
    private Long categoryId;

    @Schema(description = "是否发布")
    private Integer isPublish = 2;

    @Schema(description = "是否原创 0：转载 1:原创")
    private Integer isOriginal;

    @Schema(description = "转发地址")
    private String originalUrl;

    @Schema(description = "文章内容")
    private String content;

    @Schema(description = "文章内容MD版")
    private String contentMd;

    @Schema(description = "关键词")
    private String keywords;

    @Schema(name = "userId", description = "用户id")
    private String userId;

    @Schema(description = "文章标签id集合")
    private List<Long> tagList;
}
