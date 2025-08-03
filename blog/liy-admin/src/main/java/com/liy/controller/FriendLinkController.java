package com.liy.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import com.liy.annotation.OperationLogger;
import com.liy.common.ResponseResult;
import com.liy.entity.FriendLink;
import com.liy.service.FriendLinkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * <p>
 * 友情链接表 前端控制器
 * </p>
 *
 * @author blue
 * @since 2021-08-18
 */
@RestController
@RequestMapping("/system/friend")
@Tag(name = "友情链接后端-接口")
@RequiredArgsConstructor
public class FriendLinkController {

    private final FriendLinkService friendLinkService;

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @Operation(summary = "友链列表")
    public ResponseResult selectFriendLinkPage(String name, Integer status){
        return friendLinkService.selectFriendLinkPage(name,status);
    }

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @SaCheckPermission("system:friendLink:add")
    @Operation(summary = "添加友链")
    @OperationLogger(value = "添加友链")
    public ResponseResult addFriendLink(@RequestBody FriendLink friendLink){
        return friendLinkService.addFriendLink(friendLink);
    }

    @RequestMapping(value = "/update",method = RequestMethod.PUT)
    @SaCheckPermission("system:friendLink:update")
    @Operation(summary = "修改友链")
    @OperationLogger(value = "修改友链")
    public ResponseResult update(@RequestBody FriendLink friendLink){
        return friendLinkService.updateFriendLink(friendLink);
    }

    @RequestMapping(value = "/delete",method = RequestMethod.DELETE)
    @SaCheckPermission("system:friendLink:delete")
    @Operation(summary = "删除友链")
    @OperationLogger(value = "删除友链")
    public ResponseResult deleteFriendLink(@RequestBody List<Integer> ids){
        return friendLinkService.deleteFriendLink(ids);
    }

    @RequestMapping(value = "/top",method = RequestMethod.GET)
    @SaCheckPermission("system:friendLink:top")
    @Operation(summary = "置顶友链")
    @OperationLogger(value = "置顶友链")
    public ResponseResult top(Integer id){
        return friendLinkService.top(id);
    }
}

