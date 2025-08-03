package com.liy.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.liy.utils.DateUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author blue
 * @since 2022-01-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("b_feed_back")
@Schema(title="FeedBack对象", description="")
public class FeedBack implements Serializable {

    private static final long serialVersionUID=1L;

    @Schema(description = "ID")
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "用户id")
    private String userId;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "详细内容")
    private String content;

    @Schema(description = "添加时间")
      @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = DateUtil.FORMAT_STRING,timezone="GMT+8")
    private Date createTime;

    @Schema(description = "图片地址")
    private String imgUrl;

    @Schema(description = "反馈类型 1:需求 2：缺陷")
    private Integer type;

    @Schema(description = "状态 0:未解决 1：解决")
    private Integer status;

}
