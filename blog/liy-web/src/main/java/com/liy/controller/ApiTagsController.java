package com.liy.controller;


import com.liy.annotation.BusinessLogger;
import com.liy.common.ResponseResult;
import com.liy.service.ApiTagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 博客标签表 前端控制器
 * </p>
 *
 * @author blue
 * @since 2021-09-09
 */
@RestController
@RequestMapping("/v1/tag")
@Tag(name = "标签分类接口")
@RequiredArgsConstructor
public class ApiTagsController {

    private final ApiTagService tagsService;

    @BusinessLogger(value = "标签模块-用户访问页面",type = "查询",desc = "用户访问页面")
    @RequestMapping(value = "/",method = RequestMethod.GET)
    @Operation(description = "标签列表", summary = "标签列表")
    public ResponseResult selectTagList(){
        return tagsService.selectTagList();
    }

}

