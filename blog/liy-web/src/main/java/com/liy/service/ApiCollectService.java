package com.liy.service;

import com.liy.common.ResponseResult;


public interface ApiCollectService {

    /**
     * 我的收藏列表
     * @return
     */
    public ResponseResult selectCollectList();

    /**
     * 收藏文章
     * @param articleId 文章id
     * @return
     */
    public ResponseResult collect(Integer articleId);

    /**
     * 取消收藏
     * @param articleId 文章id
     * @return
     */
    public ResponseResult cancel(Integer articleId);



}
