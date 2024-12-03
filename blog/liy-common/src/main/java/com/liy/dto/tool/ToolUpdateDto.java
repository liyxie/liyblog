package com.liy.dto.tool;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.liy.utils.DateUtil;
import lombok.Data;

import java.util.Date;

/*
 * @Description: 工具链接表
 * @date: 2024-10-25
 */
@Data
public class ToolUpdateDto {

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
    private String describe;

    /**
     * 图标
     */
    private String icon;

    private Integer state;

}