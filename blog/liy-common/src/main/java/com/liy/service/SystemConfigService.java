package com.liy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liy.common.ResponseResult;
import com.liy.dto.system.UpdateSystemConfigDTO;
import com.liy.entity.SystemConfig;
import com.liy.entity.SystemFileConfig;

import java.util.List;

/**
 * <p>
 * 系统配置表 服务类
 * </p>
 *
 * @author blue
 * @since 2021-11-25
 */
public interface SystemConfigService extends IService<SystemConfig> {

    /**
     * 获取系统配置
     * @return
     */
    ResponseResult getSystemConfig();

    /**
     * 修改
     * @param systemConfig
     * @return
     */
    ResponseResult updateSystemConfig(UpdateSystemConfigDTO systemConfig);

    /**
     *
     * @return
     */
    SystemConfig getCustomizeOne();

    /**
     * 查询全部文件存储配置信息
     * @return List
     */
    List<SystemFileConfig> getAllSystemFileConfig();

    /**
     * 获取当前文件存储配置
     * @return
     */
    Integer getNowSystemFileConfig();
}
