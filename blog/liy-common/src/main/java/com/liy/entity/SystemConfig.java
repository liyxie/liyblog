package com.liy.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.liy.dto.system.UpdateSystemConfigDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 系统配置表
 * </p>
 *
 * @author blue
 * @since 2021-11-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(title="SystemConfig对象", description="系统配置表")
@TableName("b_system_config")
public class SystemConfig implements Serializable {

    private static final long serialVersionUID=1L;

    @Schema(description = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "是否开启邮件通知(0:否， 1:是)")
    private String startEmailNotification;

    @Schema(description = "是否开启仪表盘通知(0:否， 1:是)")
    private String openDashboardNotification;

    @Schema(description = "仪表盘通知【用于首次登录弹框】")
    private String dashboardNotification;

    @Schema(description = "仪表盘通知【用于首次登录弹框】MD")
    private String dashboardNotificationMd;

    @Schema(description = "搜索模式【0:SQL搜索 、1：全文检索】")
    private int searchModel;

    @Schema(description = "是否开启注册用户邮件激活(0:否， 1:是)")
    private String openEmailActivate;

    @Schema(description = "邮箱地址")
    private String emailHost;
    @Schema(description = "邮箱发件人")
    private String emailUsername;
    @Schema(description = "邮箱授权码")
    private String emailPassword;
    @Schema(description = "邮箱端口")
    private int emailPort;
    @Schema(description = "启用邮箱发送")
    private int openEmail;

    @Schema(description = "文件上传方式 0:本地 1：七牛云")
    private int fileUploadWay;

    @Schema(description = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @Schema(description = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;


    public static SystemConfig DtoToPo(UpdateSystemConfigDTO systemConfigDto) {
        if (systemConfigDto == null) {
            return null;
        }
        SystemConfig systemConfig = new SystemConfig();
        systemConfig.setId(systemConfigDto.getId());
        systemConfig.setStartEmailNotification(systemConfigDto.getStartEmailNotification());
        systemConfig.setOpenDashboardNotification(systemConfigDto.getOpenDashboardNotification());
        systemConfig.setDashboardNotification(systemConfigDto.getDashboardNotification());
        systemConfig.setDashboardNotificationMd(systemConfigDto.getDashboardNotificationMd());
        systemConfig.setSearchModel(systemConfigDto.getSearchModel());
        systemConfig.setOpenEmailActivate(systemConfigDto.getOpenEmailActivate());
        systemConfig.setEmailHost(systemConfigDto.getEmailHost());
        systemConfig.setEmailUsername(systemConfigDto.getEmailUsername());
        systemConfig.setEmailPassword(systemConfigDto.getEmailPassword());
        systemConfig.setEmailPort(systemConfigDto.getEmailPort());
        systemConfig.setOpenEmail(systemConfigDto.getOpenEmail());
        systemConfig.setFileUploadWay(systemConfigDto.getFileUploadWay());
        return systemConfig;
    }
}
