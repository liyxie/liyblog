package com.liy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liy.common.ResponseResult;
import com.liy.entity.Resource;

import java.util.List;

public interface ResourceService extends IService<Resource> {

    /**
     * 查询列表
     */
    ResponseResult selectResourceList(String type);

    /**
     * 根据id查询
     */
    ResponseResult selectResourceById(Integer id);

    /**
     * 保存
     */
    ResponseResult addResource(Resource resource);

    /**
     * 修改
     */
    ResponseResult updateResource(Resource resource);

    /**
     * 删除
     */
    ResponseResult deleteResourceByIds(List<Long> ids);
}
