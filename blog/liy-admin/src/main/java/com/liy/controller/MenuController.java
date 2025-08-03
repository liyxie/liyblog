package com.liy.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.liy.annotation.OperationLogger;
import com.liy.common.ResponseResult;
import com.liy.entity.Menu;
import com.liy.service.MenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 * @author blue
 * @description: 后台系统菜单管理控制器
 * @date 2021/7/30 17:12
 */
@RestController
@RequestMapping("/system/menu")
@Schema(title = "系统菜单管理-接口")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @GetMapping(value = "/getMenuTree")
    @Schema(description = "获取菜单树", httpMethod = "GET", response = ResponseResult.class, notes = "获取菜单树")
    public ResponseResult selectMenuTreeList() {
        return menuService.selectMenuTreeList(menuService.list());
    }

    @GetMapping(value = "/getMenuOptions")
    @Schema(description = "获取下拉菜单树", httpMethod = "GET", response = ResponseResult.class, notes = "获取下拉菜单树")
    public ResponseResult getMenuOptions() {
        return menuService.getMenuOptions();
    }


    @GetMapping(value = "/info/{id}")
    @Schema(description = "菜单详情", httpMethod = "GET", response = ResponseResult.class, notes = "菜单详情")
    public ResponseResult selectMenuById(@PathVariable Integer id) {
        return ResponseResult.success(menuService.getById(id));
    }

    @PostMapping(value = "/add")
    @SaCheckPermission("system:menu:add")
    @Schema(description = "添加菜单", httpMethod = "POST", response = ResponseResult.class, notes = "添加菜单")
    @OperationLogger(value = "添加菜单")
    public ResponseResult addMenu(@RequestBody Menu menu) {
        return menuService.addMenu(menu);
    }

    @PutMapping(value = "/update")
    @SaCheckPermission("system:menu:update")
    @Schema(description = "修改菜单", httpMethod = "PUT", response = ResponseResult.class, notes = "修改菜单")
    @OperationLogger(value = "修改菜单")
    public ResponseResult updateMenu(@RequestBody Menu menu) {
        return menuService.updateMenu(menu);
    }

    @DeleteMapping(value = "/delete/{id}")
    @SaCheckPermission("system:menu:delete")
    @Schema(description = "删除菜单", httpMethod = "DELETE", response = ResponseResult.class, notes = "删除菜单")
    @OperationLogger(value = "删除菜单")
    public ResponseResult deleteMenu(@PathVariable Integer id) {
        return menuService.deleteMenu(id);
    }
}
