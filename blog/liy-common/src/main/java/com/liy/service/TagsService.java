package com.liy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liy.common.ResponseResult;
import com.liy.entity.Tags;

import java.util.List;

/**
 * <p>
 * 博客标签表 服务类
 * </p>
 *
 * @author blue
 * @since 2021-09-09
 */
public interface TagsService extends IService<Tags> {

    /**
     * 分页
     * @param name
     * @return
     */
    ResponseResult selectTagsPage(String name);

    /**
     * 添加
     * @param tags
     * @return
     */
    ResponseResult addTags(Tags tags);

    /**
     * 修改
     * @param tags
     * @return
     */
    ResponseResult updateTag(Tags tags);

    /**
     * 删除
     * @param ids
     * @return
     */
    ResponseResult deleteTags(List<Long> ids);

    /**
     * 详情
     * @param id
     * @return
     */
    ResponseResult getTagsById(Long id);

}
