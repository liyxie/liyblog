package com.liy.service;

import com.liy.common.ResponseResult;
import com.liy.entity.Say;

public interface ApiSayService {


    /**
     * 说说列表
     * @return
     */
    ResponseResult selectSayList();


    /**
     * 添加说说说
     * @param say
     * @return
     */
    ResponseResult insertSay(Say say);

}
