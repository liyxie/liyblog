package com.liy.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import com.liy.annotation.OperationLogger;
import com.liy.common.ResponseResult;
import com.liy.entity.Dict;
import com.liy.service.DictService;
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
 * 字典表 前端控制器
 * </p>
 *
 * @author blue
 * @since 2021-11-25
 */
@RestController
@RequestMapping("/system/dict")
@Tag(name = "字典类型管理")
@RequiredArgsConstructor
public class DictController {

    private final DictService dictService;

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @Operation(summary = "字典类型列表")
    public ResponseResult selectDictPage(String name, Integer status){
        return dictService.selectDictPage(name,status);
    }

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @SaCheckPermission("system:dict:add")
    @Operation(summary = "添加字典")
    @OperationLogger(value = "添加字典")
    public ResponseResult insert(@RequestBody Dict dict){
        return dictService.addDict(dict);
    }

    @RequestMapping(value = "/update",method = RequestMethod.PUT)
    @SaCheckPermission("system:dict:update")
    @Operation(summary = "修改字典")
    @OperationLogger(value = "修改字典")
    public ResponseResult update(@RequestBody Dict dict){
        return dictService.updateDict(dict);
    }

    @RequestMapping(value = "/delete",method = RequestMethod.DELETE)
    @SaCheckPermission("system:dict:delete")
    @Operation(summary = "批量删除字典")
    @OperationLogger(value = "批量删除字典")
    public ResponseResult deleteBatch(@RequestBody List<Long> list){
        return dictService.deleteDict(list);
    }
}

