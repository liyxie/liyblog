package com.liy.controller;


import com.liy.common.ResponseResult;
import com.liy.service.impl.HomeServiceImpl;
import com.liy.vo.service.SystemHardwareInfoVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/system/home")
@Tag(name = "后台首页")
@RequiredArgsConstructor
public class HomeController {

    private final HomeServiceImpl homeService;

    @GetMapping(value = "/getDashboardBottomStatistics")
    @Operation(summary = "首页各种统计信息")
    public ResponseResult getDashboardBottomStatistics() {
        return ResponseResult.success(homeService.getDashboardBottomStatistics());
    }

    @GetMapping(value = "/getDashboardTopStatistics")
    @Operation(summary = "仪表盘文章、ip用户、留言统计")
    public ResponseResult getDashboardTopStatistics() {
        return ResponseResult.success(homeService.getDashboardTopStatistics());
    }

    @GetMapping(value = "/systemInfo")
    @Operation(summary = "服务器监控")
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
