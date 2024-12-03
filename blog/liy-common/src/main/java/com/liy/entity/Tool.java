package com.liy.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.liy.dto.tool.ToolAddDto;
import com.liy.dto.tool.ToolUpdateDto;
import com.liy.utils.DateUtil;
import lombok.Data;

import java.util.Date;

/*
 * @Description: 工具链接表
 * @date: 2024-10-25
 */
@Data
@TableName(value = "b_tool")
public class Tool {
    /*
    *id
    */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /*
    *名称
    */
    private String name;
    /*
    *父工具分类id
    */
    private Integer parentId;
    /*
    *排序号
    */
    private Integer sort;
    /*
    *1:工具链接实体;2:工具分类
    */
    private Integer type;
    /*
    *链接
    */
    private String url;

    /**
     * 描述
     *
     */
    @TableField("`describe`")
    private String describe;


    /**
     * 状态
     */
    private Integer state;

    /**
     * 创建者
     */
    private Integer createBy;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = DateUtil.FORMAT_STRING,timezone="GMT+8")
    private Date createTime;

    /**
     * 菜单等级
     */
    private Integer grade;

    /**
     * 图标
     */
    private String icon;

    /**
     * 删除
     */
    private Integer isDelete;

    public static Tool DtoToPo(ToolAddDto toolAddDto) {
        Tool tool = new Tool();
         tool.setName(toolAddDto.getName());
         tool.setParentId(toolAddDto.getParentId());
         tool.setSort(toolAddDto.getSort());
         tool.setType(toolAddDto.getType());
         tool.setUrl(toolAddDto.getUrl());
         tool.setDescribe(toolAddDto.getDescribe());
         tool.setCreateBy(toolAddDto.getCreateBy());
         tool.setCreateTime(toolAddDto.getCreateTime());
         tool.setGrade(toolAddDto.getGrade());
         tool.setIcon(toolAddDto.getIcon());
         return tool;
    }

    public static Tool DtoToPo(ToolUpdateDto toolAddDto) {
        Tool tool = new Tool();
        tool.setId(toolAddDto.getId());
        tool.setName(toolAddDto.getName());
        tool.setParentId(toolAddDto.getParentId());
        tool.setSort(toolAddDto.getSort());
        tool.setType(toolAddDto.getType());
        tool.setUrl(toolAddDto.getUrl());
        tool.setDescribe(toolAddDto.getDescribe());
        tool.setIcon(toolAddDto.getIcon());
        tool.setState(toolAddDto.getState());
        return tool;
    }

}