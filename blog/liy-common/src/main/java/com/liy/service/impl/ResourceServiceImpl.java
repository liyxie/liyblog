package com.liy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liy.common.ResponseResult;
import com.liy.entity.Resource;
import com.liy.mapper.ResourceMapper;
import com.liy.service.ResourceService;
import com.liy.utils.PageUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, Resource> implements ResourceService {

    private final ResourceMapper resourceMapper;

    @Override
    public ResponseResult selectResourceList(String type) {
        LambdaQueryWrapper<Resource> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(type)) {
            queryWrapper.eq(Resource::getType,type);
        }
        Page<Resource> resourcePage = resourceMapper.selectPage(new Page<>(PageUtil.getPageNo(), PageUtil.getPageSize()),queryWrapper);
        return ResponseResult.success(resourcePage);
    }


    @Override
    public  ResponseResult selectResourceById(Integer id) {
        Resource Resource = resourceMapper.selectById(id);
        return ResponseResult.success(Resource);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult addResource(Resource resource) {
        resourceMapper.insert(resource);
        return ResponseResult.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult updateResource(Resource resource) {
        resourceMapper.updateById(resource);
        return ResponseResult.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult deleteResourceByIds(List<Long> ids) {
        resourceMapper.deleteBatchIds(ids);
        return ResponseResult.success();
    }
}
