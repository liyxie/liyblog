package com.liy.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import com.liy.annotation.OperationLogger;
import com.liy.common.ResponseResult;
import com.liy.service.MessageService;
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
 *  前端控制器
 * </p>
 *
 * @author blue
 * @since 2021-09-26
 */
@RestController
@RequestMapping("/system/message")
@Tag(name = "留言管理-接口")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @RequestMapping(value="/list",method = RequestMethod.GET)
    @Operation(summary = "留言列表")
    public ResponseResult selectMessagePage(String name){
        return messageService.selectMessagePage(name);
    }

    @RequestMapping(value = "/delete",method = RequestMethod.DELETE)
    @SaCheckPermission("system:message:delete")
    @OperationLogger(value = "批量删除留言")
    @Operation(summary = "批量删除留言")
    public ResponseResult deleteBatch(@RequestBody List<Integer> ids){
        return messageService.deleteMessage(ids);
    }
}

