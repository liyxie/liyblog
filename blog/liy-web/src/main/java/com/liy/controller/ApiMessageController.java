package com.liy.controller;


import com.liy.annotation.BusinessLogger;
import com.liy.common.ResponseResult;
import com.liy.entity.Message;
import com.liy.service.ApiMessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author blue
 * @since 2021-09-03
 */
@RestController
@RequestMapping("/v1/message")
@Tag(name = "评论留言接口")
@RequiredArgsConstructor
public class ApiMessageController {

    private final ApiMessageService messageService;

    @BusinessLogger(value = "留言模块-留言列表",type = "查询",desc = "留言列表")
    @RequestMapping(value = "/",method = RequestMethod.GET)
    @Operation(description = "留言列表", summary = "留言列表")
    public ResponseResult selectMessageList(){
        return messageService.selectMessageList();
    }


    @BusinessLogger(value = "留言模块-用户留言",type = "添加",desc = "用户留言")
    @RequestMapping(value = "/",method = RequestMethod.POST)
    @Operation(description = "添加留言", summary = "添加留言")
    public ResponseResult addMessage(@RequestBody Message message){
        return messageService.addMessage(message);
    }

}

