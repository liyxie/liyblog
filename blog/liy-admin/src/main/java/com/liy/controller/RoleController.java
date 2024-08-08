package com.liy.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.liy.annotation.OperationLogger;
import com.liy.common.ResponseResult;
import com.liy.dto.role.RoleMenuDTO;
import com.liy.entity.Role;
import com.liy.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/system/role")
@Api(tags = "角色管理-接口")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;


    @RequestMapping(value = "list", method = RequestMethod.GET)
    @ApiOperation(value = "角色列表", httpMethod = "GET", response = ResponseResult.class, notes = "角色列表")
    public ResponseResult selectRolePage(String name) {
        return roleService.selectRolePage(name);
    }


    @RequestMapping(value = "queryUserRole", method = RequestMethod.GET)
    @ApiOperation(value = "获取当前登录用户所拥有的权限", httpMethod = "GET", response = ResponseResult.class, notes = "获取当前登录用户所拥有的权限")
    public ResponseResult getCurrentUserRole() {
        return roleService.getCurrentUserRole();
    }


    @RequestMapping(value = "getRoleMenuIds", method = RequestMethod.GET)
    @ApiOperation(value = "获取该角色所有的权限", httpMethod = "GET", response = ResponseResult.class, notes = "获取该角色所有的权限")
    public ResponseResult selectRoleMenuById(Integer roleId) {
        return roleService.selectRoleMenuById(roleId);
    }

    @SaCheckPermission("system:role:assign")
    @RequestMapping(value = "updateRoleMenus", method = RequestMethod.PUT)
    @ApiOperation(value = "分配角色权限", httpMethod = "PUT", response = ResponseResult.class, notes = "分配角色权限")
    public ResponseResult assignRoleMenus(@RequestBody RoleMenuDTO roleMenuDTO) {
        return roleService.assignRoleMenus(roleMenuDTO);
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    @SaCheckPermission("system:role:add")
    @ApiOperation(value = "添加角色", httpMethod = "POST", response = ResponseResult.class, notes = "添加角色")
    @OperationLogger(value = "添加角色")
    public ResponseResult addRole(@RequestBody Role role) {
        return roleService.addRole(role);
    }

    @RequestMapping(value = "update", method = RequestMethod.PUT)
    @SaCheckPermission("system:role:update")
    @ApiOperation(value = "修改角色", httpMethod = "PUT", response = ResponseResult.class, notes = "修改角色")
    @OperationLogger(value = "修改角色")
    public ResponseResult updateRole(@RequestBody Role role) {
        return roleService.updateRole(role);
    }

    @RequestMapping(value = "delete", method = RequestMethod.DELETE)
    @SaCheckPermission("system:role:delete")
    @ApiOperation(value = "删除角色", httpMethod = "DELETE", response = ResponseResult.class, notes = "删除角色")
    @OperationLogger(value = "删除角色")
    public ResponseResult deleteRole(@RequestBody List<Integer> ids) {
        return roleService.deleteRole(ids);
    }


}
