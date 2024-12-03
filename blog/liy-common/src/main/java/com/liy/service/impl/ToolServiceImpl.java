package com.liy.service.impl;


import cn.dev33.satoken.stp.StpUtil;
import com.liy.common.ResponseResult;
import com.liy.dto.tool.ToolAddDto;
import com.liy.dto.tool.ToolDto;
import com.liy.dto.tool.ToolUpdateDto;
import com.liy.entity.Menu;
import com.liy.entity.Tool;
import com.liy.mapper.ToolMapper;
import com.liy.service.ToolService;
import com.liy.vo.menu.MenuOptionsVO;
import com.liy.vo.tool.ToolMenuVo;
import com.liy.vo.tool.ToolOptionsVo;
import com.liy.vo.tool.ToolSystemVo;
import com.liy.vo.tool.ToolVo;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/*
 * @Description: 工具链接表
 * @date: 2024-10-25
 */
@Service
public class ToolServiceImpl extends ServiceImpl<ToolMapper, Tool> implements ToolService {

    @Autowired
    private ToolMapper toolMapper;


    @Override
    public List<ToolMenuVo> doMenuTree(List<Tool> toolList) {
        return buildMenuTree(toolList, ToolMenuVo::PoToVo);
    }


    public List<ToolSystemVo> doSystemTree(List<Tool> toolList) {
        return buildMenuTree(toolList, ToolSystemVo::PoToVo);
    }

    public ResponseResult selectAllToolTree() {
        List<Tool> toolList = toolMapper.selectAllToolList();
        List<ToolSystemVo> toolSystemVos = doSystemTree(toolList);
        return ResponseResult.success(toolSystemVos);
    }

    @Override
    public ResponseResult selectToolTree(ToolDto toolDto) {
        if(toolDto.isNull()){
            return selectAllToolTree();
        }

        List<Tool> toolList = toolMapper.selectFind(toolDto);
        if(toolList.isEmpty()) return ResponseResult.success(toolList);
        List<ToolSystemVo> toolSystemVos = Stream.of(ToolSystemVo.PoToVo(toolList.get(0))).collect(Collectors.toList());
        if(toolList.size() == 1) return ResponseResult.success(toolSystemVos);
        return ResponseResult.success(buildFindTree(toolSystemVos, toolSystemVos.get(0), toolList, ToolSystemVo::PoToVo, 1));
    }

    @Override
    public ResponseResult addTool(ToolAddDto toolAddDto) {
        Tool tool = Tool.DtoToPo(toolAddDto);
        tool.setCreateBy(Integer.parseInt((String) StpUtil.getLoginId()));
        if(tool.getParentId() == null || tool.getParentId() == 0){
            tool.setGrade(1);
        }else {
            Tool pTool = this.getById(tool.getParentId());
            tool.setGrade(pTool.getGrade() + 1);
        }
        boolean f = this.save(tool);
        return ResponseResult.check(f);
    }

    @Override
    public ResponseResult selectToolById(String id) {

        return null;
    }

    @Override
    public ResponseResult updateTool(ToolUpdateDto toolUpdateDto) {
        Tool tool = Tool.DtoToPo(toolUpdateDto);
        boolean f = this.updateById(tool);
        return ResponseResult.check(f);
    }

    @Override
    public ResponseResult deleteTool(String id) {
        // 原设计批量，但前端没需求，先保留
        List<String> ids = Stream.of(id).collect(Collectors.toList());
        toolMapper.deleteByIdState(ids);
        return ResponseResult.success();
    }

    @Override
    public ResponseResult getToolOptions() {
        List<Tool> list = baseMapper.selectToolMenu();
        if(list.isEmpty()) return ResponseResult.success();
        List<ToolOptionsVo> resultList = Stream.of(ToolOptionsVo.PoToVo(list.get(0))).collect(Collectors.toList());
//        resultList = buildFindTree(resultList, list.get(0), list, ToolOptionsVo::PoToVo, 1);
        return ResponseResult.success(buildFindTree(resultList, resultList.get(0), list, ToolOptionsVo::PoToVo, 1));
    }

    public <V extends ToolVo<V>> List<V> buildMenuTree(List<Tool> menuList, Function<Tool, V> mapper) {
        // 获取所有的顶级菜单（parentId 为 null 或 0 的菜单）
        List<V> rootMenu = menuList.stream()
                .filter(menu -> menu.getParentId() == null || menu.getParentId() == 0)
                .sorted(Comparator.comparing(Tool::getSort))
                .map(mapper)
                .collect(Collectors.toList());
        if(rootMenu.isEmpty()) {
            rootMenu = menuList.stream()
                    .filter(menu -> menu.getType() == 1)
                    .sorted(Comparator.comparing(Tool::getSort))
                    .map(mapper)
                    .collect(Collectors.toList());
        }
        // 遍历顶级菜单，并递归设置子菜单
        for (V root : rootMenu) {
            setChildMenus(root, menuList, mapper);
        }
        return rootMenu;
    }

    /**
     * 组件树结构 递归
     * @param rootList 树数据
     * @param nowTool 开头对象
     * @param menuList 数据集
     * @param mapper 转换函数
     * @param i 下标
     * @return
     * @param <V>
     */
    public <V extends ToolVo<V>> List<V> buildFindTree(List<V> rootList, V nowTool, List<Tool> menuList, Function<Tool, V> mapper, int i) {
        if(i >= menuList.size()) return rootList;
        Tool tool = menuList.get(i);
        // 节点
        if(tool.getType() == 1){
            if(nowTool.getType() == 2){
                nowTool.setChildToolList(mapper.apply(tool));
            }
            else {
                rootList.add(mapper.apply(tool));
            }
            return rootList;
        }
        // 菜单
        else {
            // 下级菜单
            if(tool.getParentId() != null && tool.getParentId() == nowTool.getId()){
                nowTool.setChildToolList(mapper.apply(tool));
            }
            else {
                rootList.add(mapper.apply(tool));
            }
            return buildFindTree(rootList, mapper.apply(tool), menuList, mapper, i+1);
        }
    }

    private <V extends ToolVo<V>> void setChildMenus(V parent, List<Tool> menuList, Function<Tool, V> mapper) {
        // 获取该 parent 的所有子菜单
        List<V> childMenus = menuList.stream()
                .filter(menu -> parent.getId().equals(menu.getParentId()))
                .sorted(Comparator.comparing(Tool::getSort))
                .map(mapper)
                .collect(Collectors.toList());
        parent.setChildToolList(childMenus);
        // 递归查找子菜单的子菜单
        for (V child : childMenus) {
            setChildMenus(child, menuList, mapper);
        }
    }




}
