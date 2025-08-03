package com.liy.controller;


import com.liy.common.ResponseResult;
import com.liy.service.ApiHomeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author blue
 * @since 2021-09-03
 */
@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
@Tag(name = "门户首页管理")
public class ApiHomeController {

    private final ApiHomeService homeService;


    @RequestMapping(value = "/report",method = RequestMethod.GET)
    @Operation(description = "增加访问量", summary = "增加访问量")
    public ResponseResult report(){
        return homeService.report();
    }

    @GetMapping("/webSiteInfo")
    @Operation(description = "网站相关信息", summary = "网站相关信息")
    public ResponseResult getWebSiteInfo(){
        return homeService.getWebSiteInfo();
    }

    @GetMapping("/")
    @Operation(description = "首页共享数据", summary = "首页共享数据")
    public ResponseResult getHomeData(){
        return homeService.getHomeData();
    }

    @GetMapping("/hot")
    @Operation(description = "获取各大平台热搜", summary = "获取各大平台热搜")
    public ResponseResult hot(String type){
        if(Objects.equals(type, "xianbao")) return homeService.hotXianBao(type);
        if(Objects.equals(type, "doying")) return homeService.hotDoYing(type);
        return homeService.hot(type);
    }

}

