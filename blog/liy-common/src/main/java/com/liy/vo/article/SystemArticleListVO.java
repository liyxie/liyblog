package com.liy.vo.article;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.liy.utils.DateUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @author blue
 * @date 2022/2/28
 * @apiNote
 */
@Data
public class SystemArticleListVO {

    @Schema(description = "主键id")
    private Long id;

    @Schema(description = "用户id")
    private String userId;

    @Schema(description = "文章标题")
    private String title;

    @Schema(description = "所属用户昵称")
    private String nickname;

    @Schema(description = "文章封面地址")
    private String avatar;

    @Schema(description = "阅读方式 0无需验证 1：评论阅读 2：点赞阅读 3：扫码阅读")
    private Integer readType;

    @Schema(description = "是否置顶 0否 1是")
    private Integer isStick;

    @Schema(description = "是否原创 0：转载 1:原创")
    private Integer isOriginal;

    @Schema(description = "文章阅读量")
    private Integer quantity;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = DateUtil.FORMAT_STRING,timezone="GMT+8")
    private Date createTime;

    @Schema(description = "状态")
    private Integer isPublish;

    @Schema(description = "分类名")
    private String categoryName;

    @Schema(description = "标签名集合")
    private String tagNames;
}
