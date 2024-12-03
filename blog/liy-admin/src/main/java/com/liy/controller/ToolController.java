package com.liy.controller;

import com.liy.common.ResponseResult;
import com.liy.dto.tool.ToolAddDto;
import com.liy.dto.tool.ToolDto;
import com.liy.dto.tool.ToolUpdateDto;
import com.liy.service.ToolService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分类处理器
 * @author liy
 */
@Api(tags = "工具链接API-V1")
@RestController
@RequestMapping("/system/tool")
@RequiredArgsConstructor
public class ToolController {

    private final ToolService toolService;

    @RequestMapping(value = "/toolTree",method = RequestMethod.GET)
    @ApiOperation(value = "工具列表", httpMethod = "GET", response = ResponseResult.class, notes = "工具列表")
    public ResponseResult selectToolTree(ToolDto toolDto){
        return toolService.selectToolTree(toolDto);
    }

    @GetMapping(value = "/getToolOptions")
    @ApiOperation(value = "获取下拉菜单树", httpMethod = "GET", response = ResponseResult.class, notes = "获取下拉菜单树")
    public ResponseResult getToolOptions() {
        return toolService.getToolOptions();
    }

    @PostMapping(value = "/add")
    @ApiOperation(value = "添加工具", httpMethod = "POST", response = ResponseResult.class, notes = "添加工具")
    public ResponseResult addUser(@RequestBody ToolAddDto toolAddDto) {
        return toolService.addTool(toolAddDto);
    }

    @GetMapping(value = "/info/{id}")
    @ApiOperation(value = "工具详情", httpMethod = "GET", response = ResponseResult.class, notes = "工具详情")
    public ResponseResult selectUserById(@PathVariable String id) {
        return toolService.selectToolById(id);
    }

    @PutMapping(value = "/update")
    @ApiOperation(value = "修改工具", httpMethod = "PUT", response = ResponseResult.class, notes = "修改工具")
    public ResponseResult updateUser(@RequestBody ToolUpdateDto toolUpdateDto) {
        return toolService.updateTool(toolUpdateDto);
    }

    @RequestMapping(value = "/delete",method = RequestMethod.DELETE)
    @ApiOperation(value = "删除工具", httpMethod = "DELETE", response = ResponseResult.class, notes = "删除工具")
    public ResponseResult deleteTool(@RequestBody String id) {
        return toolService.deleteTool(id);
    }


}
