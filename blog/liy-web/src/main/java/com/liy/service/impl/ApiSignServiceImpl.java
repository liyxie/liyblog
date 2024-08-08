package com.liy.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.liy.common.ResponseResult;
import com.liy.entity.Sign;
import com.liy.exception.BusinessException;
import com.liy.mapper.SignMapper;
import com.liy.service.ApiSignService;
import com.liy.utils.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApiSignServiceImpl implements ApiSignService {

    private final SignMapper signMapper;


    /**
     * 用户的签到记录
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    @Override
    public ResponseResult getSignRecords(String startTime, String endTime) {
        List<String> list = signMapper.getSignRecordsByUserId(startTime,endTime,StpUtil.getLoginIdAsString());
        return ResponseResult.success(list);
    }

    /**
     * 用户签到
     * @param time 签到时间
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult sign(String time) {
        String userId = StpUtil.getLoginIdAsString();

        Sign sign = signMapper.selctSignByUserIdAndTime(userId,time);
        if (sign != null) {
            throw new BusinessException("当日已签到，无需再次签到！");
        }

        sign = Sign.builder().userId(userId).createTime(DateUtil.strToDateTime(time,DateUtil.YYYY_MM_DD)).build();
        signMapper.insert(sign);

        return ResponseResult.success();
    }

    /**
     * 验证用户当日是否签到
     * @return
     */
    @Override
    public ResponseResult validateTodayIsSign() {
        String today = DateUtil.dateTimeToStr(DateUtil.getNowDate(), DateUtil.YYYY_MM_DD);
        Sign sign = signMapper.validateTodayIsSign(today,StpUtil.getLoginIdAsString());
        return ResponseResult.success(sign);
    }
}
