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
 * 
 * </p>
 *
 * @author blue
 * @since 2021-11-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(title="BExceptionLog对象", description="")
@TableName("b_exception_log")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionLog implements Serializable {

    private static final long serialVersionUID=1L;

    @Schema(description = "主键")
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "ip")
    private String ip;

    @Schema(description = "ip来源")
    private String ipSource;

    @Schema(description = "请求方法")
    private String method;

    @Schema(description = "描述")
    private String operation;

    @Schema(description = "参数")
    private String params;

    @Schema(description = "异常对象json格式")
    private String exceptionJson;

    @Schema(description = "异常简单信息,等同于e.getMessage")
    private String exceptionMessage;

    @Schema(description = "发生时间")
      @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = DateUtil.FORMAT_STRING,timezone="GMT+8")
    private Date createTime;


}
