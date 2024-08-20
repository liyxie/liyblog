package com.liy.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.liy.dto.system.UpdateSystemConfigDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value="SystemConfig对象", description="系统配置表")
@TableName("b_system_config")
public class SystemConfig implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "是否开启邮件通知(0:否， 1:是)")
    private String startEmailNotification;

    @ApiModelProperty(value = "是否开启仪表盘通知(0:否， 1:是)")
    private String openDashboardNotification;

    @ApiModelProperty(value = "仪表盘通知【用于首次登录弹框】")
    private String dashboardNotification;

    @ApiModelProperty(value = "仪表盘通知【用于首次登录弹框】MD")
    private String dashboardNotificationMd;

    @ApiModelProperty(value = "搜索模式【0:SQL搜索 、1：全文检索】")
    private int searchModel;

    @ApiModelProperty(value = "是否开启注册用户邮件激活(0:否， 1:是)")
    private String openEmailActivate;

    @ApiModelProperty(value = "邮箱地址")
    private String emailHost;
    @ApiModelProperty(value = "邮箱发件人")
    private String emailUsername;
    @ApiModelProperty(value = "邮箱授权码")
    private String emailPassword;
    @ApiModelProperty(value = "邮箱端口")
    private int emailPort;
    @ApiModelProperty(value = "启用邮箱发送")
    private int openEmail;

    @ApiModelProperty(value = "文件上传方式 0:本地 1：七牛云")
    private int fileUploadWay;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
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
