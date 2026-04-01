package com.liy.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liy.config.FileConfig;
import com.liy.service.ApiSayService;
import com.liy.common.Constants;
import com.liy.common.ResponseResult;
import com.liy.common.ResultCode;
import com.liy.entity.Say;
import com.liy.exception.BusinessException;
import com.liy.handle.RelativeDateFormat;
import com.liy.mapper.SayMapper;
import com.liy.utils.PageUtil;
import com.liy.vo.say.ApiSayVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ApiSayServiceImpl implements ApiSayService {

    private final SayMapper sayMapper;

    private final FileConfig fileConfig;


    @Override
    public ResponseResult selectSayList() {

        //是否显示未公开的说说 登录用户id为1时显示所有说说
        boolean showPrivate = StpUtil.isLogin() && StpUtil.hasRole(Constants.ADMIN_CODE);
        Page<ApiSayVO>  sayPage = sayMapper.selectPublicSayList(new Page<>(PageUtil.getPageNo(), PageUtil.getPageSize()),showPrivate);
        sayPage.getRecords().forEach(item -> {
            item.setCreateTimeStr(RelativeDateFormat.format(item.getCreateTime()));
            item.setAvatar(fileConfig.buildUrl(item.getAvatar()));
            if (item.getImgUrl() != null && !item.getImgUrl().isEmpty()) {
                String assembled = Arrays.stream(item.getImgUrl().split(","))
                        .map(fileConfig::buildUrl)
                        .collect(Collectors.joining(","));
                item.setImgUrl(assembled);
            }
        });
        return ResponseResult.success(sayPage);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult insertSay(Say say) {
        String userId = StpUtil.getLoginIdAsString();
        if (!userId.equals("1")) {
            throw new BusinessException(ResultCode.NO_PERMISSION);
        }
        say.setUserId(userId);
        sayMapper.insert(say);
        return ResponseResult.success();
    }
}
