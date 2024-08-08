package com.liy.controller;


import com.liy.annotation.BusinessLogger;
import com.liy.common.ResponseResult;
import com.liy.service.ApiSoftwareService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/software")
@Api(tags = "开源软件接口")
@RequiredArgsConstructor
public class ApiSoftwareController {

    private final ApiSoftwareService softwareService;

    @BusinessLogger(value = "开源软件-用户访问开源软件",type = "查询",desc = "用户访问页面")
    @RequestMapping(value = "/",method = RequestMethod.GET)
    @ApiOperation(value = "开源软件", httpMethod = "GET", response = ResponseResult.class, notes = "开源软件")
    public ResponseResult selectSoftwareList(){
        return softwareService.selectSoftwareList();
    }
}
