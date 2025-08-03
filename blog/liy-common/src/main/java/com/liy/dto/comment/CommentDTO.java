package com.liy.dto.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;


/**
 * @author blue
 * @date 2021/12/28
 * @apiNote
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "评论")
public class CommentDTO {

    /**
     * 回复用户id
     */
    @Schema(name = "replyUserId", description = "回复用户id", type = "Integer")
    private Integer replyUserId;

    /**
     * 评论文章id
     */
    @Schema(name = "articleId", description = "文章id", type = "Integer")
    private Integer articleId;

    /**
     * 评论内容
     */
    @NotBlank(message = "评论内容不能为空")
    @Schema(name = "commentContent", description = "评论内容", required = true, type = "String")
    private String commentContent;

    /**
     * 父评论id
     */
    @Schema(name = "parentId", description = "评论父id", type = "Integer")
    private Integer parentId;

    /**
     * 父评论id
     */
    @Schema(name = "userId", description = "用户ID", type = "Long")
    private Long userId;


}
