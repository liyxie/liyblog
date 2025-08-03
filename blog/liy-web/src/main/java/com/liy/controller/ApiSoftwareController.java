package com.liy.controller;


import com.liy.annotation.BusinessLogger;
import com.liy.common.ResponseResult;
import com.liy.service.ApiSoftwareService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/software")
@Tag(name = "开源软件接口")
@RequiredArgsConstructor
public class ApiSoftwareController {

    private final ApiSoftwareService softwareService;

    @BusinessLogger(value = "开源软件-用户访问开源软件",type = "查询",desc = "用户访问页面")
    @RequestMapping(value = "/",method = RequestMethod.GET)
    @Operation(description = "开源软件", summary = "开源软件")
    public ResponseResult selectSoftwareList(){
        return softwareService.selectSoftwareList();
    }
}
