package com.liy.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.liy.annotation.OperationLogger;
import com.liy.common.ResponseResult;
import com.liy.dto.user.SystemUserDTO;
import com.liy.dto.user.UserPasswordDTO;
import com.liy.entity.User;
import com.liy.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author blue
 * @description:
 * @date 2021/7/30 17:12
 */
@RestController
@RequestMapping("/system/user")
@Api(tags = "系统用户管理-接口")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping(value = "/list")
    @ApiOperation(value = "用户列表", httpMethod = "GET", response = ResponseResult.class, notes = "用户列表")
    public ResponseResult selectUserPage(String username, Integer loginType) {
        return userService.selectUserPage(username,loginType);
    }

    @PostMapping(value = "/add")
    @SaCheckPermission("system:user:add")
    @ApiOperation(value = "添加用户", httpMethod = "POST", response = ResponseResult.class, notes = "添加用户")
    @OperationLogger(value = "添加用户")
    public ResponseResult addUser(@RequestBody SystemUserDTO user) {
        return userService.addUser(user);
    }

    @GetMapping(value = "/info/{id}")
    @ApiOperation(value = "用户详情", httpMethod = "GET", response = ResponseResult.class, notes = "用户详情")
    public ResponseResult selectUserById(@PathVariable String id) {
        return userService.selectUserById(id);
    }

    @PutMapping(value = "/update")
    @SaCheckPermission("system:user:update")
    @ApiOperation(value = "修改用户", httpMethod = "PUT", response = ResponseResult.class, notes = "修改用户")
    @OperationLogger(value = "修改用户")
    public ResponseResult updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

    @RequestMapping(value = "/delete",method = RequestMethod.DELETE)
    @SaCheckPermission("system:user:delete")
    @ApiOperation(value = "删除用户", httpMethod = "DELETE", response = ResponseResult.class, notes = "删除用户")
    @OperationLogger(value = "删除用户")
    public ResponseResult deleteUSer(@RequestBody List<String> ids) {
        return userService.deleteUSer(ids);
    }

    @GetMapping(value = "/getCurrentUserInfo")
    @ApiOperation(value = "获取当前登录用户信息", httpMethod = "GET", response = ResponseResult.class, notes = "获取当前登录用户信息")
    public ResponseResult getCurrentUserInfo() {
        return userService.getCurrentUserInfo();
    }

    @GetMapping(value = "/getUserMenu")
    @ApiOperation(value = "获取用户菜单", httpMethod = "GET", response = ResponseResult.class, notes = "获取用户菜单")
    public ResponseResult getCurrentUserMenu() {
        return userService.getCurrentUserMenu();
    }

    @PostMapping(value = "/updatePassword")
    @SaCheckPermission("system:user:updatePassword")
    @ApiOperation(value = "修改密码", httpMethod = "POST", response = ResponseResult.class, notes = "修改密码")
    @OperationLogger(value = "修改密码")
    public ResponseResult updatePassword(@RequestBody UserPasswordDTO userPasswordDTO) {
        return userService.updatePassword(userPasswordDTO);
    }

    @GetMapping(value = "/online")
    @ApiOperation(value = "查看在线用户", httpMethod = "GET", response = ResponseResult.class, notes = "查看在线用户")
    public ResponseResult listOnlineUsers(String keywords) {
        return userService.listOnlineUsers(keywords);
    }

    @GetMapping(value = "/kick")
    @SaCheckPermission("system:user:kick")
    @OperationLogger(value = "踢人下线")
    @ApiOperation(value = "踢人下线", httpMethod = "GET", response = ResponseResult.class, notes = "踢人下线")
    public ResponseResult kick(String token) {
        return userService.kick(token);
    }
}
