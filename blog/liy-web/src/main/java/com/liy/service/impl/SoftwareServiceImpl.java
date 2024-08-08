package com.liy.service.impl;

import com.liy.common.ResponseResult;
import com.liy.entity.Software;
import com.liy.mapper.SoftwareMapper;
import com.liy.service.ApiSoftwareService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SoftwareServiceImpl implements ApiSoftwareService {

    private final SoftwareMapper softwareMapper;

    @Override
    public ResponseResult selectSoftwareList() {
        List<Software> softwares = softwareMapper.selectList(null);
        return ResponseResult.success(softwares);
    }
}
