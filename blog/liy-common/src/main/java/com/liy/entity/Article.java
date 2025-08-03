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
 * 博客文章表
 * </p>
 *
 * @author blue
 * @since 2021-08-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("b_article")
@Schema(title="BlogArticle对象", description="博客文章表")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Article implements Serializable {

    private static final long serialVersionUID=1L;

    @Schema(description = "主键id")
      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "用户id")
    private String userId;

    @Schema(description = "分类id")
    private Long categoryId;

    @Schema(description = "文章标题")
    private String title;

    @Schema(description = "文章封面地址")
    private String avatar;

    @Schema(description = "文章简介")
    private String summary;

    @Schema(description = "文章内容")
    private String content;

    @Schema(description = "文章内容MD版")
    private String contentMd;

    @Schema(description = "发布状态 0：下架；1：上架")
    private Integer isPublish;

    @Schema(description = "阅读方式 0无需验证 1：评论阅读 2：点赞阅读 3：扫码阅读")
    private Integer readType;

    @Schema(description = "是否置顶 0否 1是")
    private Integer isStick;

    @Schema(description = "是否原创 0：转载 1:原创")
    private Integer isOriginal;

    @Schema(description = "转发地址")
    private String originalUrl;

    @Schema(description = "关键词")
    private String keywords;

    @Schema(description = "发布地址")
    private String address;

    @Schema(description = "文章阅读量")
    private Integer quantity;

    @Schema(description = "是否首页轮播")
    private Integer isCarousel;

    @Schema(description = "是否推荐 0否 1是")
    private Integer isRecommend;

    @Schema(description = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = DateUtil.FORMAT_STRING,timezone="GMT+8")
    private Date createTime;

    @Schema(description = "最后更新时间")
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = DateUtil.FORMAT_STRING,timezone="GMT+8")
    private Date updateTime;

}
