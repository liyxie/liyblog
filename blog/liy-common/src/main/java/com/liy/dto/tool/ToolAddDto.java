package com.liy.dto.tool;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.liy.utils.DateUtil;
import lombok.Data;

import java.util.Date;

/*
 * @Description: 工具链接表
 * @date: 2024-10-25
 */
@Data
public class ToolAddDto {
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
    private String describe;


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

}