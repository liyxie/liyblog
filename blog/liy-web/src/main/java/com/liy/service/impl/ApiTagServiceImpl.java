package com.liy.service.impl;

import com.liy.service.ApiTagService;
import com.liy.common.ResponseResult;
import com.liy.mapper.TagsMapper;
import com.liy.vo.tag.ApiTagListVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApiTagServiceImpl implements ApiTagService {

    private final TagsMapper tagsMapper;

    /**
     *  标签列表
     * @return
     */
    @Override
    public ResponseResult selectTagList() {
        List<ApiTagListVO> list = tagsMapper.selectTagListApi();
        return ResponseResult.success(list);
    }
}
