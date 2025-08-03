package com.liy.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.liy.common.ResponseResult;
import com.liy.entity.Say;
import com.liy.service.ApiSayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "说说接口-API")
@RequestMapping("v1/say")
@RequiredArgsConstructor
public class ApiSayController {

    private final ApiSayService apiSayService;

    @RequestMapping(value = "getSayList",method = RequestMethod.GET)
    @Operation(description = "说说列表", summary = "说说列表")
    public ResponseResult selectSayList(){
        return apiSayService.selectSayList();
    }


    @SaCheckLogin
    @RequestMapping(value = "insertSay",method = RequestMethod.POST)
    @Operation(description = "添加说说说", summary = "添加说说说")
    public ResponseResult insertSay(@RequestBody Say say){
        return apiSayService.insertSay(say);
    }

}
