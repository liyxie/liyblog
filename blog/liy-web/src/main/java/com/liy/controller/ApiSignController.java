package com.liy.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.liy.annotation.AccessLimit;
import com.liy.annotation.BusinessLogger;
import com.liy.common.ResponseResult;
import com.liy.service.ApiSignService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "签到接口-API")
@RequestMapping("v1/sign")
@RequiredArgsConstructor
public class ApiSignController {

    private final ApiSignService apiSignService;

    @SaCheckLogin
    @RequestMapping(value = "getSignRecords",method = RequestMethod.GET)
    @Operation(description = "用户签到记录", summary = "用户签到记录")
    public ResponseResult getSignRecords(String startTime,String endTime){
        return apiSignService.getSignRecords(startTime,endTime);
    }

    @AccessLimit
    @SaCheckLogin
    @RequestMapping(value = "/",method = RequestMethod.GET)
    @BusinessLogger(value = "签到-用户签到",type = "添加",desc = "用户签到")
    @Operation(description = "用户签到", summary = "用户签到")
    public ResponseResult sign(String time){
        return apiSignService.sign(time);
    }

    @SaCheckLogin
    @RequestMapping(value = "validateTodayIsSign",method = RequestMethod.GET)
    @Operation(description = "验证用户当日是否签到", summary = "验证用户当日是否签到")
    public ResponseResult validateTodayIsSign(){
        return apiSignService.validateTodayIsSign();
    }

}
