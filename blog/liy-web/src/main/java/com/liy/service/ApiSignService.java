package com.liy.service;

import com.liy.common.ResponseResult;

public interface ApiSignService {


    /**
     * 用户的签到记录
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    ResponseResult getSignRecords(String startTime, String endTime);


    /**
     * 用户签到
     * @param time 签到时间
     * @return
     */
    ResponseResult sign(String time);

    /**
     * 验证用户当日是否签到
     * @return
     */
    ResponseResult validateTodayIsSign();


}
