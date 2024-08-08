package com.liy.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liy.common.Constants;
import com.liy.common.ResponseResult;
import com.liy.entity.SystemConfig;
import com.liy.mapper.SystemConfigMapper;
import com.liy.service.SystemConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * <p>
 * 系统配置表 服务实现类
 * </p>
 *
 * @author blue
 * @since 2021-11-25
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SystemConfigServiceImpl extends ServiceImpl<SystemConfigMapper, SystemConfig> implements SystemConfigService {


    /**
     * 获取系统配置
     * @return
     */
    @Override
    public ResponseResult getSystemConfig() {
        LambdaQueryWrapper<SystemConfig> wrapper = new LambdaQueryWrapper<SystemConfig>().last(Constants.LIMIT_ONE);

        if (!StpUtil.hasRole(Constants.ADMIN_CODE)){
            wrapper.select(SystemConfig::getId,SystemConfig::getOpenEmail,SystemConfig::getOpenEmailActivate,SystemConfig::getStartEmailNotification,
                    SystemConfig::getFileUploadWay,SystemConfig::getDashboardNotification,SystemConfig::getDashboardNotificationMd,
                    SystemConfig::getOpenDashboardNotification,SystemConfig::getSearchModel,SystemConfig::getLocalFileUrl,
                    SystemConfig::getEmailHost,SystemConfig::getEmailPort);
        }

        SystemConfig systemConfig = baseMapper.selectOne(wrapper);

        return ResponseResult.success(systemConfig);
    }

    /**
     * 修改系统配置
     * @param systemConfig
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult updateSystemConfig(SystemConfig systemConfig) {
        baseMapper.updateById(systemConfig);
        return ResponseResult.success();
    }

    //---------自定义方法----------
    @Override
    public SystemConfig getCustomizeOne() {
        return baseMapper.selectOne(new QueryWrapper<SystemConfig>().last(Constants.LIMIT_ONE));
    }
}
