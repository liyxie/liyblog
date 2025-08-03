package com.liy.entity;

import com.baomidou.mybatisplus.annotation.*;
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
@TableName("b_comment")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(title="Comment对象", description="评论实体类")
public class Comment implements Serializable {

    private static final long serialVersionUID=1L;


    @Schema(description = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    @Schema(description = "评论用户Id")
    private String userId;

    /**
     * 回复用户id
     */
    @Schema(description = "回复用户id")
    private String replyUserId;

    /**
     * 评论文章id
     */
    @Schema(description = "文章id")
    private Integer articleId;

    /**
     * 评论内容
     */
    @Schema(description = "评论内容")
    private String content;

    /**
     * 父评论id
     */
    @Schema(description = "父级id")
    private Integer parentId;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @Schema(description = "浏览器名")
    private String browser;

    @Schema(description = "浏览器版本")
    private String browserVersion;

    @Schema(description = "系统名")
    @TableField("`system`")
    private String system;

    @Schema(description = "系统版本")
    private String systemVersion;
    @Schema(description = "ip地址")
    private String ipAddress;



}
