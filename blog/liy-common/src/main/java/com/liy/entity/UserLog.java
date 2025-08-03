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
 * 日志表
 * </p>
 *
 * @author blue
 * @since 2021-11-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(title="UserLog对象", description="日志表")
@TableName("b_user_log")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLog implements Serializable {

    private static final long serialVersionUID=1L;

    @Schema(description = "主键ID")
      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "ip地址")
    private String ip;

    @Schema(description = "操作地址")
    private String address;

    @Schema(description = "操作类型")
    private String type;

    @Schema(description = "操作日志")
    private String description;

    @Schema(description = "操作模块")
    private String model;

    @Schema(description = "操作系统")
    private String accessOs;
    @Schema(description = "客户端类型")
    private String clientType;

    @Schema(description = "浏览器")
    private String browser;

    @Schema(description = "操作时间")
      @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = DateUtil.FORMAT_STRING,timezone="GMT+8")
    private Date createTime;

    @Schema(description = "操作结果")
    private String result;


}
