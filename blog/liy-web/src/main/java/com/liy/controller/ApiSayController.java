package com.liy.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.liy.common.ResponseResult;
import com.liy.entity.Say;
import com.liy.service.ApiSayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "说说接口-API")
@RequestMapping("v1/say")
@RequiredArgsConstructor
public class ApiSayController {

    private final ApiSayService apiSayService;

    @RequestMapping(value = "getSayList",method = RequestMethod.GET)
    @ApiOperation(value = "说说列表", httpMethod = "GET", response = ResponseResult.class, notes = "说说列表")
    public ResponseResult selectSayList(){
        return apiSayService.selectSayList();
    }


    @SaCheckLogin
    @RequestMapping(value = "insertSay",method = RequestMethod.POST)
    @ApiOperation(value = "添加说说说", httpMethod = "POST", response = ResponseResult.class, notes = "添加说说说")
    public ResponseResult insertSay(@RequestBody Say say){
        return apiSayService.insertSay(say);
    }

}
