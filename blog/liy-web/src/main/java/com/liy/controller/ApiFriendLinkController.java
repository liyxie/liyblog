package com.liy.controller;


import com.liy.annotation.BusinessLogger;
import com.liy.common.ResponseResult;
import com.liy.entity.FriendLink;
import com.liy.service.ApiFriendLinkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 友情链接表 前端控制器
 * </p>
 *
 * @author blue
 * @since 2021-08-18
 */
@Tag(name = "友情链接API-V1")
@RestController
@RequestMapping("/v1/link")
@RequiredArgsConstructor
public class ApiFriendLinkController {

    private final ApiFriendLinkService friendLinkService;

    @BusinessLogger(value = "友链模块-用户访问页面",type = "查询",desc = "友链列表")
    @RequestMapping(value = "/",method = RequestMethod.GET)
    @Operation(description = "友链列表", summary = "友链列表")
    public ResponseResult selectFriendLinkList(){
        return friendLinkService.selectFriendLinkList();
    }

    @BusinessLogger(value = "友链模块-用户申请友链",type = "添加",desc = "用户申请友链")
    @RequestMapping(value = "/",method = RequestMethod.POST)
    @Operation(description = "申请友链", summary = "申请友链")
    public ResponseResult addFriendLink(@RequestBody FriendLink friendLink){
        return friendLinkService.addFriendLink(friendLink);
    }



}

