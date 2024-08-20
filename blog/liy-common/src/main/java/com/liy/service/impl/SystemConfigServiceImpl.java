package com.liy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liy.common.Constants;
import com.liy.common.ResponseResult;
import com.liy.config.FileConfig;
import com.liy.dto.system.UpdateSystemConfigDTO;
import com.liy.entity.SystemConfig;
import com.liy.entity.SystemFileConfig;
import com.liy.mapper.SystemConfigMapper;
import com.liy.mapper.SystemFileConfigMapper;
import com.liy.service.SystemConfigService;
import com.liy.strategy.context.FileUploadStrategyContext;
import com.liy.vo.system.SystemConfigVO;
import com.liy.vo.system.SystemFileConfigVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


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


    private final SystemFileConfigMapper systemFileConfigMapper;
    private final SystemConfigMapper systemConfigMapper;
    private final FileUploadStrategyContext fileUploadStrategyContext;
    private final FileConfig fileConfig;
    /**
     * 获取系统配置
     * @return
     */
    @Override
    public ResponseResult getSystemConfig() {
        // 系统配置信息
        SystemConfig systemConfig = baseMapper.selectOne(null);

        // 文件存储配置信息
        List<SystemFileConfig> systemFileConfigList = getAllSystemFileConfig();

        SystemConfigVO systemConfigVO = SystemConfigVO.poToVo(systemConfig);
        List<SystemFileConfigVO> systemFileConfigVOList = systemFileConfigList.stream().filter(Objects::nonNull).map(SystemFileConfigVO::poToVo).collect(Collectors.toList());
        systemConfigVO.setSystemFileConfigVOList(systemFileConfigVOList);

        return ResponseResult.success(systemConfigVO);
    }

    /**
     * 修改系统配置
     * @param systemConfig
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult updateSystemConfig(UpdateSystemConfigDTO systemConfig) {
        // 更新系统配置
        SystemConfig newSystemConfig = SystemConfig.DtoToPo(systemConfig);
        int i = baseMapper.updateById(newSystemConfig);

        // 更新文件上传
        List<SystemFileConfig> systemFileConfigList = systemConfig.getSystemFileConfigVOList().stream()
                .filter(Objects::nonNull)
                .map(SystemFileConfig::voToPo)
                .collect(Collectors.toList());

        int j = systemFileConfigMapper.updateBatchById(systemFileConfigList);

        // 更新当前文件存储配置
        fileConfig.init();
        fileUploadStrategyContext.init();

        return ResponseResult.check(i, j);
    }

    //---------自定义方法----------
    @Override
    public SystemConfig getCustomizeOne() {
        return systemConfigMapper.selectOne(new QueryWrapper<SystemConfig>().last(Constants.LIMIT_ONE));
    }

    @Override
    public List<SystemFileConfig> getAllSystemFileConfig() {
        return systemFileConfigMapper.selectList(null);
    }

    @Override
    public Integer getNowSystemFileConfig() {
        return systemConfigMapper.selectOne(null).getFileUploadWay();
    }
}
