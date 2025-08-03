package com.liy.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("b_collect")
@Schema(title="Collect", description="")
public class Collect implements Serializable {
    private static final long serialVersionUID=1L;


    @Schema(description = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    @Schema(description = "评论用户Id")
    private String userId;

    /**
     * 评论文章id
     */
    @Schema(description = "文章id")
    private Integer articleId;


    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
}
