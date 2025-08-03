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
 * 博客标签表
 * </p>
 *
 * @author blue
 * @since 2021-09-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("b_tags")
@Schema(title="Tags对象", description="博客标签表")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Tags implements Serializable {

    private static final long serialVersionUID=1L;

    @Schema(description = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "标签名称")
    private String name;

    @Schema(description = "排序")
    private int sort;

    @Schema(description = "点击量")
    private int clickVolume;

    @Schema(description = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = DateUtil.FORMAT_STRING,timezone="GMT+8")
    private Date createTime;

    @Schema(description = "最后更新时间")
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = DateUtil.FORMAT_STRING,timezone="GMT+8")
    private Date updateTime;


    public Tags(Long id, int clickVolume) {
        this.id = id;
        this.clickVolume = clickVolume;
    }
}
