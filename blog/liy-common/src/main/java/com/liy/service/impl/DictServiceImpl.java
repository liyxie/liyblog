package com.liy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liy.common.Constants;
import com.liy.common.ResponseResult;
import com.liy.entity.Dict;
import com.liy.entity.DictData;
import com.liy.exception.BusinessException;
import com.liy.mapper.DictMapper;
import com.liy.service.DictDataService;
import com.liy.service.DictService;
import com.liy.utils.PageUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;



/**
 * <p>
 * 字典表 服务实现类
 * </p>
 *
 * @author blue
 * @since 2021-11-25
 */
@Service
@RequiredArgsConstructor(onConstructor_ = {@Lazy})
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

    private final DictDataService dictDataService;

    /**
     * 字典列表
     * @param name
     * @return
     */
    @Override
    public ResponseResult selectDictPage(String name, Integer status) {

        Page data = baseMapper.selectPage(new Page<>(PageUtil.getPageNo(), PageUtil.getPageSize()), new LambdaQueryWrapper<Dict>()
                .eq(status != null,Dict::getStatus,status)
                .like(StringUtils.isNotBlank(name), Dict::getName,name)
                .orderByDesc(Dict::getSort));
        return ResponseResult.success(data);
    }

    /**
     * 添加字典
     * @param dict
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult addDict(Dict dict) {
        validateTypeIsExist(dict.getType());
        baseMapper.insert(dict);
        return ResponseResult.success();
    }

    /**
     * 修改字典
     * @param dict
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult updateDict(Dict dict) {
        Dict temp = baseMapper.selectById(dict.getId());
        if (!temp.getType().equals(dict.getType())){
            validateTypeIsExist(dict.getType());
        }
        baseMapper.updateById(dict);
        return ResponseResult.success();
    }


    /**
     * 批量删除字典
     * @param ids
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult deleteDict(List<Long> ids) {
        int count  = dictDataService.count(new LambdaQueryWrapper<DictData>().in(DictData::getDictId,ids));
        if (count > 0) {
            throw new BusinessException("所选字典类型中存在字典数据！");
        }
        baseMapper.deleteBatchIds(ids);
        return ResponseResult.success();
    }


    /* ---------自定义方法开始------------*/
    public void validateTypeIsExist(String type){
        Dict temp  = baseMapper.selectOne(new LambdaQueryWrapper<Dict>().eq(Dict::getType, type).last(Constants.LIMIT_ONE));
        if (temp != null) {
            throw new BusinessException("字典标签已存在！");
        }
    }
}
