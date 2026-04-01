package com.liy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liy.config.FileConfig;
import com.liy.service.FeedBackService;
import com.liy.common.ResponseResult;
import com.liy.entity.FeedBack;
import com.liy.mapper.FeedBackMapper;
import com.liy.utils.PageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author blue
 * @since 2022-01-13
 */
@Service
@RequiredArgsConstructor
public class FeedBackServiceImpl extends ServiceImpl<FeedBackMapper, FeedBack> implements FeedBackService {

    private final FileConfig fileConfig;

    /**
     * 反馈列表
     * @param type
     * @return
     */
    @Override
    public ResponseResult selectFeedBackPage(Integer type) {
        Page<FeedBack> feedBackPage = baseMapper.selectPage(new Page<>(PageUtil.getPageNo(), PageUtil.getPageSize()), new LambdaQueryWrapper<FeedBack>()
                .orderByDesc(FeedBack::getCreateTime).eq(type != null, FeedBack::getType, type));
        feedBackPage.getRecords().forEach(fb -> fb.setImgUrl(fileConfig.buildUrl(fb.getImgUrl())));
        return ResponseResult.success(feedBackPage);
    }

    /**
     * 删除反馈
     * @param ids
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult deleteFeedBack(List<Integer> ids) {
        baseMapper.deleteByIds(ids);
        return ResponseResult.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult updateFeedBack(FeedBack feedBack) {
        baseMapper.updateById(feedBack);
        return ResponseResult.success();
    }

}
