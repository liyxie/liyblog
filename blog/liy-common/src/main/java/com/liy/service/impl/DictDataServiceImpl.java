package com.liy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liy.common.ResponseResult;
import com.liy.common.ResultCode;
import com.liy.entity.Dict;
import com.liy.entity.DictData;
import com.liy.enums.YesOrNoEnum;
import com.liy.exception.BusinessException;
import com.liy.mapper.DictDataMapper;
import com.liy.service.DictDataService;
import com.liy.service.DictService;
import com.liy.utils.PageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * 字典数据表 服务实现类
 * </p>
 *
 * @author blue
 * @since 2021-11-25
 */
@Service
@RequiredArgsConstructor(onConstructor_ = {@Lazy})
public class DictDataServiceImpl extends ServiceImpl<DictDataMapper, DictData> implements DictDataService {

    private final DictService dictService;

    /**
     * 获取字典数据列表
     * @param dictId
     * @param isPublish
     * @return
     */
    @Override
    public ResponseResult selectDictDataPage(Integer dictId, Integer isPublish) {
        LambdaQueryWrapper<DictData> queryWrapper = new LambdaQueryWrapper<DictData>()
                .eq(DictData::getDictId,dictId).eq(isPublish != null, DictData::getStatus,isPublish);
        Page<DictData> data = baseMapper.selectPage(new Page<>(PageUtil.getPageNo(), PageUtil.getPageSize()), queryWrapper);
        data.getRecords().forEach(item ->{
            Dict dict = dictService.getById(item.getDictId());
            item.setDict(dict);
        });
        return ResponseResult.success(data);
    }

    /**
     * 添加字典数据
     * @param dictData
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult addDictData(DictData dictData) {
        // 判断添加的字典数据是否存在
        DictData temp = baseMapper.selectOne(new LambdaQueryWrapper<DictData>()
                .eq(DictData::getLabel, dictData.getLabel())
                .eq(DictData::getDictId, dictData.getDictId()));
        if (temp != null) {
            throw new BusinessException(ResultCode.DATA_TAG_IS_EXIST);
        }
        baseMapper.insert(dictData);
        return ResponseResult.success();
    }

    /**
     * 修改字典数据
     * @param sysDictData
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult updateDictData(DictData sysDictData) {

        DictData dictData = baseMapper.selectOne(new LambdaQueryWrapper<DictData>().eq(DictData::getLabel,sysDictData.getLabel()));
        if (dictData != null && !dictData.getId().equals(sysDictData.getId())){
           throw new BusinessException(ResultCode.DATA_TAG_IS_EXIST);
        }

        baseMapper.updateById(sysDictData);
        return ResponseResult.success();
    }

    /**
     * 批量删除字典数据
     * @param
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult deleteDictData(List<Long> ids) {
        baseMapper.deleteByIds(ids);
        return ResponseResult.success();
    }


    /**
     * 根据字典类型获取字典数据
     * @param types
     * @return
     */
    @Override
    public ResponseResult getDataByDictType(List<String> types) {
        Map<String, Map<String, Object>> map = new HashMap<>();

        List<Dict> dictList = dictService.list(new LambdaQueryWrapper<Dict>().in(Dict::getType,types).eq(Dict::getStatus, YesOrNoEnum.YES.getCode()));
        dictList.forEach(item ->{
            LambdaQueryWrapper<DictData> sysDictDataQueryWrapper = new LambdaQueryWrapper<DictData>();
            sysDictDataQueryWrapper.eq(DictData::getStatus,YesOrNoEnum.YES.getCode());
            sysDictDataQueryWrapper.eq(DictData::getDictId, item.getId());
            sysDictDataQueryWrapper.orderByAsc(DictData::getSort);
            List<DictData> dataList = baseMapper.selectList(sysDictDataQueryWrapper);
            String defaultValue = null;
            for (DictData dictData : dataList) {
                //选取默认值
                if (dictData.getIsDefault().equals(YesOrNoEnum.YES.getCodeToString())){
                    defaultValue = dictData.getValue();
                    break;

                }
            }
            Map<String, Object> result = new HashMap<>();
            result.put("defaultValue",defaultValue);
            result.put("list",dataList);
            map.put(item.getType(),result);
        });
        return ResponseResult.success(map);
    }

}
