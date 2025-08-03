package com.liy.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.liy.annotation.OperationLogger;
import com.liy.common.ResponseResult;
import com.liy.entity.Menu;
import com.liy.service.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 * @author blue
 * @description: 后台系统菜单管理控制器
 * @date 2021/7/30 17:12
 */
@RestController
@RequestMapping("/system/menu")
@Tag(name = "系统菜单管理-接口")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @GetMapping(value = "/getMenuTree")
    @Operation(summary = "获取菜单树")
    public ResponseResult selectMenuTreeList() {
        return menuService.selectMenuTreeList(menuService.list());
    }

    @GetMapping(value = "/getMenuOptions")
    @Operation(summary = "获取下拉菜单树")
    public ResponseResult getMenuOptions() {
        return menuService.getMenuOptions();
    }


    @GetMapping(value = "/info/{id}")
    @Operation(summary = "菜单详情")
    public ResponseResult selectMenuById(@PathVariable Integer id) {
        return ResponseResult.success(menuService.getById(id));
    }

    @PostMapping(value = "/add")
    @SaCheckPermission("system:menu:add")
    @Operation(summary = "添加菜单")
    @OperationLogger(value = "添加菜单")
    public ResponseResult addMenu(@RequestBody Menu menu) {
        return menuService.addMenu(menu);
    }

    @PutMapping(value = "/update")
    @SaCheckPermission("system:menu:update")
    @Operation(summary = "修改菜单")
    @OperationLogger(value = "修改菜单")
    public ResponseResult updateMenu(@RequestBody Menu menu) {
        return menuService.updateMenu(menu);
    }

    @DeleteMapping(value = "/delete/{id}")
    @SaCheckPermission("system:menu:delete")
    @Operation(summary = "删除菜单")
    @OperationLogger(value = "删除菜单")
    public ResponseResult deleteMenu(@PathVariable Integer id) {
        return menuService.deleteMenu(id);
    }
}
