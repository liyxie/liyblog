package com.liy.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.liy.annotation.AccessLimit;
import com.liy.annotation.BusinessLogger;
import com.liy.common.ResponseResult;
import com.liy.service.ApiSignService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "签到接口-API")
@RequestMapping("v1/sign")
@RequiredArgsConstructor
public class ApiSignController {

    private final ApiSignService apiSignService;

    @SaCheckLogin
    @RequestMapping(value = "getSignRecords",method = RequestMethod.GET)
    @ApiOperation(value = "用户签到记录", httpMethod = "GET", response = ResponseResult.class, notes = "用户签到记录")
    public ResponseResult getSignRecords(String startTime,String endTime){
        return apiSignService.getSignRecords(startTime,endTime);
    }

    @AccessLimit
    @SaCheckLogin
    @RequestMapping(value = "/",method = RequestMethod.GET)
    @BusinessLogger(value = "签到-用户签到",type = "添加",desc = "用户签到")
    @ApiOperation(value = "用户签到", httpMethod = "GET", response = ResponseResult.class, notes = "用户签到")
    public ResponseResult sign(String time){
        return apiSignService.sign(time);
    }

    @SaCheckLogin
    @RequestMapping(value = "validateTodayIsSign",method = RequestMethod.GET)
    @ApiOperation(value = "验证用户当日是否签到", httpMethod = "GET", response = ResponseResult.class, notes = "验证用户当日是否签到")
    public ResponseResult validateTodayIsSign(){
        return apiSignService.validateTodayIsSign();
    }

}
