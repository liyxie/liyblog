package com.liy.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import com.liy.annotation.OperationLogger;
import com.liy.common.ResponseResult;
import com.liy.entity.DictData;
import com.liy.service.DictDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 字典数据表 前端控制器
 * </p>
 *
 * @author blue
 * @since 2021-11-25
 */
@RestController
@RequestMapping("/system/dictData")
@Api(tags = "字典数据管理")
@RequiredArgsConstructor
public class DictDataController {

    private final DictDataService dictDataService;

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @ApiOperation(value = "字典数据列表", httpMethod = "GET", response = ResponseResult.class, notes = "字典数据列表")
    public ResponseResult selectDictDataPage(Integer dictId, Integer isPublish){
        return dictDataService.selectDictDataPage(dictId,isPublish);
    }

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @SaCheckPermission("system:dictData:add")
    @ApiOperation(value = "添加字典数据", httpMethod = "POST", response = ResponseResult.class, notes = "添加字典数据")
    @OperationLogger(value = "添加字典数据")
    public ResponseResult addDictData(@RequestBody DictData dictData){
        return dictDataService.addDictData(dictData);
    }

    @RequestMapping(value = "/update",method = RequestMethod.PUT)
    @SaCheckPermission("system:dictData:update")
    @ApiOperation(value = "修改字典数据", httpMethod = "PUT", response = ResponseResult.class, notes = "修改字典数据")
    @OperationLogger(value = "修改字典数据")
    public ResponseResult update(@RequestBody DictData dictData){
        return dictDataService.updateDictData(dictData);
    }

    @RequestMapping(value = "/deleteBatch",method = RequestMethod.DELETE)
    @SaCheckPermission("system:dictData:delete")
    @ApiOperation(value = "批量删除字典数据", httpMethod = "DELETE", response = ResponseResult.class, notes = "批量删除字典数据")
    @OperationLogger(value = "批量删除字典数据")
    public ResponseResult deleteDictData(@RequestBody List<Long> ids){
        return dictDataService.deleteDictData(ids);
    }

    @RequestMapping(value = "/getDataByDictType",method = RequestMethod.POST)
    public ResponseResult getDataByDictType(@RequestBody List<String> types){
        return dictDataService.getDataByDictType(types);
    }
}

