package com.liy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liy.config.FileConfig;
import com.liy.service.WebConfigService;
import com.liy.common.Constants;
import com.liy.common.ResponseResult;
import com.liy.entity.WebConfig;
import com.liy.mapper.WebConfigMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 网站配置表 服务实现类
 * </p>
 *
 * @author blue
 * @since 2021-11-27
 */
@Service
@RequiredArgsConstructor
public class WebConfigServiceImpl extends ServiceImpl<WebConfigMapper, WebConfig> implements WebConfigService {

    private final FileConfig fileConfig;

    /**
     * 网站配置
     * @return
     */
    @Override
    public ResponseResult getWebConfig() {
        WebConfig webConfig = baseMapper.selectOne(new QueryWrapper<WebConfig>()
                .last(Constants.LIMIT_ONE));
        if (webConfig != null) {
            webConfig.setLogo(fileConfig.buildUrl(webConfig.getLogo()));
            webConfig.setAuthorAvatar(fileConfig.buildUrl(webConfig.getAuthorAvatar()));
            webConfig.setTouristAvatar(fileConfig.buildUrl(webConfig.getTouristAvatar()));
            webConfig.setAliPay(fileConfig.buildUrl(webConfig.getAliPay()));
            webConfig.setWeixinPay(fileConfig.buildUrl(webConfig.getWeixinPay()));
        }
        return ResponseResult.success(webConfig);
    }

    /**
     * 修改网站配置
     * @param webConfig
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult updateWebConfig(WebConfig webConfig) {
        webConfig.setLogo(fileConfig.stripDomain(webConfig.getLogo()));
        webConfig.setAuthorAvatar(fileConfig.stripDomain(webConfig.getAuthorAvatar()));
        webConfig.setTouristAvatar(fileConfig.stripDomain(webConfig.getTouristAvatar()));
        webConfig.setAliPay(fileConfig.stripDomain(webConfig.getAliPay()));
        webConfig.setWeixinPay(fileConfig.stripDomain(webConfig.getWeixinPay()));
        baseMapper.updateById(webConfig);
        return ResponseResult.success();
    }
}
