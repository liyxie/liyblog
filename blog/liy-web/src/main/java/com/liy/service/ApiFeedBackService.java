package com.liy.service;

import com.liy.common.ResponseResult;
import com.liy.entity.FeedBack;

public interface ApiFeedBackService {
    /**
     * 添加反馈
     * @return
     */
    public ResponseResult addFeedback(FeedBack feedBack);


}
