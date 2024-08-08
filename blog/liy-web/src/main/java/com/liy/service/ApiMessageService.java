package com.liy.service;


import com.liy.common.ResponseResult;
import com.liy.entity.Message;

public interface ApiMessageService {

    /**
     * 获取所有留言
     * @return
     */
    ResponseResult selectMessageList();

    /**
     * 添加留言
     * @param message
     * @return
     */
    ResponseResult addMessage(Message message);
}
