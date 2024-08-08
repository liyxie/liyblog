package com.liy.controller;


import com.liy.common.ResponseResult;
import com.liy.service.impl.HomeServiceImpl;
import com.liy.vo.service.SystemHardwareInfoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/system/home")
@Api(tags = "后台首页")
@RequiredArgsConstructor
public class HomeController {

    private final HomeServiceImpl homeService;

    @GetMapping(value = "/getDashboardBottomStatistics")
    @ApiOperation(value = "首页各种统计信息", httpMethod = "GET", response = ResponseResult.class, notes = "首页各种统计信息")
    public ResponseResult getDashboardBottomStatistics() {
        return ResponseResult.success(homeService.getDashboardBottomStatistics());
    }

    @GetMapping(value = "/getDashboardTopStatistics")
    @ApiOperation(value = "仪表盘文章、ip用户、留言统计", httpMethod = "GET", response = ResponseResult.class, notes = "首页文章、ip用户、留言统计")
    public ResponseResult getDashboardTopStatistics() {
        return ResponseResult.success(homeService.getDashboardTopStatistics());
    }

    @GetMapping(value = "/systemInfo")
    @ApiOperation(value = "服务器监控", httpMethod = "GET", response = ResponseResult.class, notes = "服务器监控")
    public ResponseResult systemInfo() {
        SystemHardwareInfoVO systemHardwareInfoVO = new SystemHardwareInfoVO();
        try {
            systemHardwareInfoVO.copyTo();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseResult.success(systemHardwareInfoVO);
    }
}
