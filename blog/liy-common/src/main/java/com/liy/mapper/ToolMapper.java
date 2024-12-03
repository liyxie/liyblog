package com.liy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liy.dto.tool.ToolDto;
import com.liy.entity.Tool;

import java.util.List;

/*
 * @Description: 工具链接表
 * @date: 2024-10-25
 */
public interface ToolMapper extends BaseMapper<Tool> {

    List<Tool> selectToolList();

    List<Tool> selectAllToolList();


    List<Tool> selectFind(ToolDto toolDto);

    void deleteByIdState(List<String> ids);

    List<Tool> selectToolMenu();
}
