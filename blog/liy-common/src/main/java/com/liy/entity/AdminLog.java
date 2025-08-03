package com.liy.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.liy.utils.DateUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author blue
 * @since 2021-11-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(title="AdminLog对象", description="")
@TableName("b_admin_log")
public class AdminLog implements Serializable {

    private static final long serialVersionUID=1L;

    @Schema(description = "主键ID")
      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "操作用户")
    private String username;

    @Schema(description = "请求接口")
    private String requestUrl;

    @Schema(description = "请求方式")
    private String type;

    @Schema(description = "操作名称")
    private String operationName;

    @Schema(description = "ip")
    private String ip;

    @Schema(description = "ip来源")
    private String source;

    @Schema(description = "请求参数")
    private String paramsJson;

    @Schema(description = "类地址")
    private String classPath;

    @Schema(description = "方法名")
    private String methodName;

    @Schema(description = "请求接口耗时")
    private Long spendTime;

    @Schema(description = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = DateUtil.FORMAT_STRING,timezone="GMT+8")
    private Date createTime;

    public AdminLog(){}

    /**
     * 构造函数
     *
     * @param ip
     * @param type
     * @param requestUrl
     * @param paramsJson
     * @param classPath
     * @param methodName
     * @param operationName
     */
    public AdminLog(String ip, String source, String type, String requestUrl, String nickname,
                    String paramsJson, String classPath,
                    String methodName, String operationName,
                 Long spendTime) {
        this.ip = ip;
        this.source = StringUtils.isBlank(source) ? "未知": source;
        this.type = type;
        this.requestUrl = requestUrl;
        this.username = nickname;
        this.paramsJson = paramsJson;
        this.classPath = classPath;
        this.methodName = methodName;
        this.operationName = operationName;
        this.spendTime = spendTime;
    }
}
