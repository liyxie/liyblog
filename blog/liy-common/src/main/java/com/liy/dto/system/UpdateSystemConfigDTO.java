package com.liy.dto.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.liy.entity.SystemConfig;
import com.liy.vo.system.SystemFileConfigVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 系统配置表
 * </p>
 *
 * @author CodeScholar
 * @date 2023年4月9日
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "SystemConfig对象", description = "系统配置表")
@TableName("b_system_config")
public class UpdateSystemConfigDTO implements Serializable {

    private static final long serialVersionUID = 1L;

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

    @ApiModelProperty(value = "文件存储方式id")
    private int fileUploadWay;

    @ApiModelProperty(value = "文件存储信息")
    private List<SystemFileConfigVO> systemFileConfigVOList;

    public static SystemConfig DtoToPo(UpdateSystemConfigDTO updateSystemConfigDTO) {
        if (updateSystemConfigDTO == null) {
            return null;
        }
        SystemConfig systemConfig = new SystemConfig();
        systemConfig.setId(updateSystemConfigDTO.getId());
        systemConfig.setStartEmailNotification(updateSystemConfigDTO.getStartEmailNotification());
        systemConfig.setOpenDashboardNotification(updateSystemConfigDTO.getOpenDashboardNotification());
        systemConfig.setDashboardNotification(updateSystemConfigDTO.getDashboardNotification());
        systemConfig.setDashboardNotificationMd(updateSystemConfigDTO.getDashboardNotificationMd());
        systemConfig.setSearchModel(updateSystemConfigDTO.getSearchModel());
        systemConfig.setOpenEmailActivate(updateSystemConfigDTO.getOpenEmailActivate());
        systemConfig.setEmailHost(updateSystemConfigDTO.getEmailHost());
        systemConfig.setEmailUsername(updateSystemConfigDTO.getEmailUsername());
        systemConfig.setEmailPassword(updateSystemConfigDTO.getEmailPassword());
        systemConfig.setEmailPort(updateSystemConfigDTO.getEmailPort());
        systemConfig.setOpenEmail(updateSystemConfigDTO.getOpenEmail());
        systemConfig.setFileUploadWay(updateSystemConfigDTO.getFileUploadWay());
        return systemConfig;
    }
}
