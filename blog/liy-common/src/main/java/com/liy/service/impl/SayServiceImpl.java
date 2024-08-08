package com.liy.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liy.service.SayService;
import com.liy.common.Constants;
import com.liy.common.ResponseResult;
import com.liy.entity.Say;
import com.liy.enums.PublishEnum;
import com.liy.mapper.SayMapper;
import com.liy.utils.PageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * <p>
 * 博客文章表 服务实现类
 * </p>
 *
 * @author blue
 * @since 2021-08-18
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SayServiceImpl extends ServiceImpl<SayMapper, Say> implements SayService {


    @Override
    public ResponseResult selectSayPage(String keywords) {
        LambdaQueryWrapper<Say> sayLambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (!StpUtil.hasRole(Constants.ADMIN_CODE)) {
            sayLambdaQueryWrapper.eq(Say::getIsPublic, PublishEnum.PUBLISH);
        }
        sayLambdaQueryWrapper.orderByDesc(Say::getCreateTime);
        Page<Say> sayPage = baseMapper.selectPage(new Page<>(PageUtil.getPageNo(), PageUtil.getPageSize()),sayLambdaQueryWrapper);
        return ResponseResult.success(sayPage);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult addSay(Say say) {
        say.setUserId(StpUtil.getLoginIdAsString());
        baseMapper.insert(say);
        return ResponseResult.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult deleteSay(List<String> ids) {
        baseMapper.deleteBatchIds(ids);
        return ResponseResult.success();
    }

    @Override
    public ResponseResult selectSayById(String id) {
        Say say = baseMapper.selectById(id);
        return ResponseResult.success(say);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult updateSay(Say say) {
        baseMapper.updateById(say);
        return ResponseResult.success();
    }
}
