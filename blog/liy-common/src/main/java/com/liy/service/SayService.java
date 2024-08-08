package com.liy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liy.common.ResponseResult;
import com.liy.entity.Say;

import java.util.List;

/**
 * <p>
 * 博客文章表 服务类
 * </p>
 *
 * @author blue
 * @since 2021-08-18
 */
public interface SayService extends IService<Say> {


    /**
     * 说说分页列表
     * @param keywords
     * @return
     */
    ResponseResult selectSayPage(String keywords);

    /**
     * 添加说说
     * @param say
     * @return
     */
    ResponseResult addSay(Say say);

    /**
     * 删除说说
     * @param ids 说说id集合
     * @return
     */
    ResponseResult deleteSay(List<String> ids);

    /**
     * 说说详情
     * @param id
     * @return
     */
    ResponseResult selectSayById(String id);

    /**
     * 修改说说
     * @param say
     * @return
     */
    ResponseResult updateSay(Say say);
}
