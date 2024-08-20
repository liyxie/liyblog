package com.liy.vo.system;

import com.liy.entity.SystemConfig;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value = "SystemConfigVO对象", description = "系统配置")
public class SystemConfigVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
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
    @ApiModelProperty(value = "邮箱端口")
    private int emailPort;
    @ApiModelProperty(value = "启用邮箱发送")
    private int openEmail;

    @ApiModelProperty(value = "文件存储方式id")
    private int fileUploadWay;

    @ApiModelProperty(value = "文件存储信息")
    private List<SystemFileConfigVO> systemFileConfigVOList;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
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
