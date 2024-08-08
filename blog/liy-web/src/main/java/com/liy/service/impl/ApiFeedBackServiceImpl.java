package com.liy.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.liy.common.ResponseResult;
import com.liy.entity.FeedBack;
import com.liy.mapper.FeedBackMapper;
import com.liy.service.ApiFeedBackService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ApiFeedBackServiceImpl implements ApiFeedBackService {

    private final FeedBackMapper feedBackMapper;

    /**
     * 添加反馈
     *
     * @param feedBack
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult addFeedback(FeedBack feedBack) {
        feedBack.setUserId(StpUtil.getLoginIdAsString());
        int rows = feedBackMapper.insert(feedBack);
        return rows > 0 ? ResponseResult.success() : ResponseResult.error("添加反馈失败");
    }
}
