package com.liy.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.liy.entity.Resource;
import com.liy.service.ResourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.liy.common.ResponseResult;

@RestController
@RequestMapping(value="/system/resource")
@Tag(name = "资源管理")
public class ResourceController {
    @Autowired
    private ResourceService resourceService;

    /**
     *列表
     **/
    @RequestMapping(value="/list", method = RequestMethod.GET)
    @Operation(summary = "资源列表")
    public ResponseResult selectResourceList(String type) {
        return resourceService.selectResourceList(type);
    }

    /**
     *详情
     **/
    @RequestMapping(value="/info/{id}", method = RequestMethod.GET)
    @Operation(summary = "资源详情")
    public ResponseResult selectResourceById(@PathVariable Integer id) {
        return resourceService.selectResourceById(id);
    }

    /**
     *添加
     **/
    @SaCheckPermission("system:resource:add")
    @RequestMapping(value="/add", method=RequestMethod.POST)
    @Operation(summary = "添加资源")
    public ResponseResult addBResource(@RequestBody Resource bResource) {
        return resourceService.addResource(bResource);
    }

    /**
     *修改
     **/
    @SaCheckPermission("system:resource:update")
    @RequestMapping(value="/update", method=RequestMethod.PUT)
    @Operation(summary = "修改资源")
    public ResponseResult updateBResource(
            @RequestBody Resource bResource) {
        return resourceService.updateResource(bResource);
    }

    /**
     *删除多条
     **/
    @SaCheckPermission("system:resource:delete")
    @RequestMapping(value="/delete", method=RequestMethod.DELETE)
    @Operation(summary = "删除资源")
    public ResponseResult deleteBResourceByIds(@RequestBody List<Long> ids) {
        return resourceService.deleteResourceByIds(ids);
    }
}
