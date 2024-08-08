package com.liy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liy.common.ResponseResult;
import com.liy.entity.Dict;

import java.util.List;

/**
 * <p>
 * 字典表 服务类
 * </p>
 *
 * @author blue
 * @since 2021-11-25
 */
public interface DictService extends IService<Dict> {

    /**
     * 分页
     * @param name
     * @param status
     * @return
     */
    ResponseResult selectDictPage(String name, Integer status);

    /**
     * 添加
     * @param dict
     * @return
     */
    ResponseResult addDict(Dict dict);

    /**
     * 修改
     * @param dict
     * @return
     */
    ResponseResult updateDict(Dict dict);

    /**
     * 删除
     * @param list
     * @return
     */
    ResponseResult deleteDict(List<Long> list);

}
