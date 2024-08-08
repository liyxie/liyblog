package com.liy.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.liy.annotation.BusinessLogger;
import com.liy.common.ResponseResult;
import com.liy.service.ApiImMessageService;
import com.liy.vo.message.ImMessageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/v1/im")
@Api(tags = "聊天管理-门户")
@RequiredArgsConstructor
public class ApiImMessageController {

    private final ApiImMessageService imMessageService;


    @SaCheckLogin
    @GetMapping(value = "/")
    @BusinessLogger(value = "首页-获取历史聊天记录",type = "查询",desc = "获取历史聊天记录")
    @ApiOperation(value = "获取历史聊天记录", httpMethod = "GET", response = ResponseResult.class, notes = "获取历史聊天记录")
    public ResponseResult selectHistoryList() {
        return  imMessageService.selectHistoryList();
    }

    @SaCheckLogin
    @GetMapping(value = "/getRoomList")
    @ApiOperation(value = "获取房间列表", httpMethod = "GET", response = ResponseResult.class, notes = "获取房间列表")
    public ResponseResult selectRoomList() {
        return imMessageService.selectRoomList();
    }

    @SaCheckLogin
    @GetMapping(value = "/selectUserImHistory")
    @BusinessLogger(value = "首页-获取单聊历史消息",type = "查询",desc = "获取单聊历史消息")
    @ApiOperation(value = "获取单聊历史消息", httpMethod = "GET", response = ResponseResult.class, notes = "获取单聊历史消息")
    public ResponseResult selectUserImHistoryList(String fromUserId,String toUserId) {
        return  imMessageService.selectUserImHistoryList(fromUserId,toUserId);
    }

    @SaCheckLogin
    @PostMapping(value = "/chat")
    @ApiOperation(value = "发送消息", httpMethod = "POST", response = ResponseResult.class, notes = "发送消息")
    public ResponseResult chat(@RequestBody ImMessageVO message){
        return imMessageService.chat(message);
    }

    @SaCheckLogin
    @PostMapping(value = "/withdraw")
    @ApiOperation(value = "撤回消息", httpMethod = "POST", response = ResponseResult.class, notes = "撤回消息")
    public ResponseResult withdraw(@RequestBody ImMessageVO message){
        return imMessageService.withdraw(message);
    }

    @SaCheckLogin
    @GetMapping(value = "/read")
    @ApiOperation(value = "已读消息", httpMethod = "GET", response = ResponseResult.class, notes = "已读消息")
    public ResponseResult read(String userId) {
        return imMessageService.read(userId);
    }

    @SaCheckLogin
    @DeleteMapping(value = "/deleteRoom")
    @ApiOperation(value = "删除房间", httpMethod = "DELETE", response = ResponseResult.class, notes = "删除房间")
    public ResponseResult deleteRoom(String roomId) {
        return imMessageService.deleteRoom(roomId);
    }

    @SaCheckLogin
    @PostMapping(value = "/addRoom")
    @ApiOperation(value = "创建房间（即点击私信按钮）", httpMethod = "POST", response = ResponseResult.class, notes = "创建房间（即点击私信按钮）")
    public ResponseResult addRoom(String userId) {
        return imMessageService.addRoom(userId);
    }

    @SaCheckLogin
    @GetMapping(value = "/getMessageNotice")
    @ApiOperation(value = "获取系统通知", httpMethod = "GET", response = ResponseResult.class, notes = "获取系统通知")
    public ResponseResult getMessageNotice(Integer type) {
        return imMessageService.getMessageNotice(type);
    }

    @SaCheckLogin
    @GetMapping("/getNewSystemNotice")
    @ApiOperation(value = "获取未读的最新通知", httpMethod = "GET", response = ResponseResult.class, notes = "获取未读的最新通知")
    public ResponseResult getNewSystemNotice(){
        return imMessageService.getNewSystemNotice();
    }

    @SaCheckLogin
    @DeleteMapping("/deleteMessage")
    @ApiOperation(value = "根据类型删除所有消息", httpMethod = "DELETE", response = ResponseResult.class, notes = "根据类型删除所有消息")
    public ResponseResult deleteMessage(String id,Integer type){
        return imMessageService.deleteByNoticeType(id,type);
    }


    @SaCheckLogin
    @GetMapping(value = "/getMessageNoticeApplet")
    @ApiOperation(value = "获取系统通知-小程序", httpMethod = "GET", response = ResponseResult.class, notes = "获取系统通知-小程序")
    public ResponseResult getMessageNoticeApplet(Integer type) {
        return imMessageService.getMessageNoticeApplet(type);
    }

    @SaCheckLogin
    @GetMapping(value = "/markReadMessageNoticeApplet")
    @ApiOperation(value = "标记已读-小程序", httpMethod = "GET", response = ResponseResult.class, notes = "标记已读-小程序")
    public ResponseResult markReadMessageNoticeApplet(String id) {
        return imMessageService.markReadMessageNoticeApplet(id);
    }

}
