package com.liy.service;

import com.liy.common.ResponseResult;


public interface ApiFollowedService {
    /**
     * 关注用户
     * @param userId 关注的用户id
     * @return
     */
    public ResponseResult addFollowedUser(String userId);

    /**
     * 关注用户
     * @param userId 关注的用户id
     * @return
     */
    public ResponseResult deleteFollowed(String userId);
}
