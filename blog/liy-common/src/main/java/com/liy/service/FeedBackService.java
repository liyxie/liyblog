package com.liy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liy.common.ResponseResult;
import com.liy.entity.FeedBack;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author blue
 * @since 2022-01-13
 */
public interface FeedBackService extends IService<FeedBack> {

    /**
     * 分页
     * @param type
     * @return
     */
    ResponseResult selectFeedBackPage(Integer type);

    /**
     * 删除
     * @param ids
     * @return
     */
    ResponseResult deleteFeedBack(List<Integer> ids);

    /**
     * 修改
     * @param feedBack
     * @return
     */
    ResponseResult updateFeedBack(FeedBack feedBack);
}
