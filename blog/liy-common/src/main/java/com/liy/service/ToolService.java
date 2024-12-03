package com.liy.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.liy.common.ResponseResult;
import com.liy.dto.tool.ToolAddDto;
import com.liy.dto.tool.ToolDto;
import com.liy.dto.tool.ToolUpdateDto;
import com.liy.entity.Tool;
import com.liy.vo.tool.ToolMenuVo;

import java.util.List;


/*
 * @Description: 工具链接表
 * @date: 2024-10-25
 */

public interface ToolService  extends IService<Tool> {


    List<ToolMenuVo> doMenuTree(List<Tool> toolList);


    ResponseResult selectToolTree(ToolDto toolDto);

    ResponseResult addTool(ToolAddDto toolAddDto);

    ResponseResult selectToolById(String id);

    ResponseResult updateTool(ToolUpdateDto toolUpdateDto);

    ResponseResult deleteTool(String ids);

    ResponseResult getToolOptions();
}
