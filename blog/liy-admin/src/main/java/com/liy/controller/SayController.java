package com.liy.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.liy.annotation.OperationLogger;
import com.liy.common.ResponseResult;
import com.liy.entity.Say;
import com.liy.service.SayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/system/say")
@Tag(name = "说说管理")
@RequiredArgsConstructor
public class SayController {

    private final SayService sayService;

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @Operation(summary = "说说列表")
    public ResponseResult selectSayPage(String keywords){
        return sayService.selectSayPage(keywords);
    }

    @RequestMapping(value = "/info",method = RequestMethod.GET)
    @Operation(summary = "说说详情")
    public ResponseResult info(String id){
        return sayService.selectSayById(id);
    }

    @OperationLogger(value = "修改说说")
    @SaCheckPermission("system:say:update")
    @RequestMapping(value = "/update",method = RequestMethod.PUT)
    @Operation(summary = "修改说说")
    public ResponseResult updateSay(@RequestBody Say say){
        return sayService.updateSay(say);
    }

    @OperationLogger(value = "发表说说")
    @SaCheckPermission("system:say:add")
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @Operation(summary = "发表说说")
    public ResponseResult addSay(@RequestBody Say say){
        return sayService.addSay(say);
    }

    @OperationLogger(value = "删除说说")
    @SaCheckPermission("system:say:delete")
    @RequestMapping(value = "/delete",method = RequestMethod.DELETE)
    @Operation(summary = "删除说说")
    public ResponseResult deleteSay(@RequestBody List<String> ids){
        return sayService.deleteSay(ids);
    }

}
