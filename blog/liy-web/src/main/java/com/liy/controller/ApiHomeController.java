package com.liy.controller;


import com.liy.common.ResponseResult;
import com.liy.service.ApiHomeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Schema(title = "门户首页管理")
public class ApiHomeController {

    private final ApiHomeService homeService;


    @RequestMapping(value = "/report",method = RequestMethod.GET)
    @Schema(description = "增加访问量", httpMethod = "GET", response = ResponseResult.class, notes = "增加访问量")
    public ResponseResult report(){
        return homeService.report();
    }

    @GetMapping("/webSiteInfo")
    @Schema(description = "网站相关信息", httpMethod = "GET", response = ResponseResult.class, notes = "网站相关信息")
    public ResponseResult getWebSiteInfo(){
        return homeService.getWebSiteInfo();
    }

    @GetMapping("/")
    @Schema(description = "首页共享数据", httpMethod = "GET", response = ResponseResult.class, notes = "首页共享数据")
    public ResponseResult getHomeData(){
        return homeService.getHomeData();
    }

    @GetMapping("/hot")
    @Schema(description = "获取各大平台热搜", httpMethod = "GET", response = ResponseResult.class, notes = "获取各大平台热搜")
    public ResponseResult hot(String type){
        if(Objects.equals(type, "xianbao")) return homeService.hotXianBao(type);
        if(Objects.equals(type, "doying")) return homeService.hotDoYing(type);
        return homeService.hot(type);
    }

}

