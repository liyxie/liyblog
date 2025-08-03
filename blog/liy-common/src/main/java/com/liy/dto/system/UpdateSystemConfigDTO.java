package com.liy.dto.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.liy.entity.SystemConfig;
import com.liy.vo.system.SystemFileConfigVO;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(title = "SystemConfig对象", description = "系统配置表")
@TableName("b_system_config")
public class UpdateSystemConfigDTO implements Serializable {

    private static final long serialVersionUID = 1L;

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

    @Schema(description = "文件存储方式id")
    private int fileUploadWay;

    @Schema(description = "文件存储信息")
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
