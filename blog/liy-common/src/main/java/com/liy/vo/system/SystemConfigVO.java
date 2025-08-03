package com.liy.vo.system;

import com.liy.entity.SystemConfig;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 系统配置信息
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(title = "SystemConfigVO对象", description = "系统配置")
public class SystemConfigVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
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
    @Schema(description = "邮箱端口")
    private int emailPort;
    @Schema(description = "启用邮箱发送")
    private int openEmail;

    @Schema(description = "文件存储方式id")
    private int fileUploadWay;

    @Schema(description = "文件存储信息")
    private List<SystemFileConfigVO> systemFileConfigVOList;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "更新时间")
    private Date updateTime;

    public static SystemConfigVO poToVo(SystemConfig systemConfig) {
        if (systemConfig == null) {
            return null;
        }
        SystemConfigVO systemConfigVO = new SystemConfigVO();
        systemConfigVO.setId(systemConfig.getId());
        systemConfigVO.setStartEmailNotification(systemConfig.getStartEmailNotification());
        systemConfigVO.setOpenDashboardNotification(systemConfig.getOpenDashboardNotification());
        systemConfigVO.setDashboardNotification(systemConfig.getDashboardNotification());
        systemConfigVO.setDashboardNotificationMd(systemConfig.getDashboardNotificationMd());
        systemConfigVO.setSearchModel(systemConfig.getSearchModel());
        systemConfigVO.setOpenEmailActivate(systemConfig.getOpenEmailActivate());
        systemConfigVO.setEmailHost(systemConfig.getEmailHost());
        systemConfigVO.setEmailUsername(systemConfig.getEmailUsername());
        systemConfigVO.setEmailPort(systemConfig.getEmailPort());
        systemConfigVO.setOpenEmail(systemConfig.getOpenEmail());
        systemConfigVO.setFileUploadWay(systemConfig.getFileUploadWay());
        systemConfigVO.setCreateTime(systemConfig.getCreateTime());
        systemConfigVO.setUpdateTime(systemConfig.getUpdateTime());
        return systemConfigVO;
    }

}
