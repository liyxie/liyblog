package com.liy.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.liy.utils.DateUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 系统管理-权限资源表 
 * </p>
 *
 * @author blue
 * @since 2021-07-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("b_menu")
@Schema(title="Menu对象", description="系统管理-权限资源表 ")
public class Menu implements Serializable {

    private static final long serialVersionUID=1L;

    @Schema(description = "主键")
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "上级资源ID")
    private Integer parentId;

    @Schema(description = "路由路径")
    private String path;

    @Schema(description = "路由重定向路径")
    private String redirect;

    @Schema(description = "组件路径")
    private String component;

    @Schema(description = "菜单名称")
    private String title;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "资源图标")
    private String icon;

    @Schema(description = "类型 menu、button")
    private String type;

    @Schema(description = "资源名字")
    private String name;

    @Schema(description = "权限标识")
    private String perm;

    @Schema(description = "是否显示")
    private Integer hidden;

    @Schema(description = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = DateUtil.FORMAT_STRING,timezone="GMT+8")
    private Date createdTime;

    @Schema(description = "最后更新时间")
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = DateUtil.FORMAT_STRING,timezone="GMT+8")
    private Date updateTime;

    @TableField(exist = false)
    private List<Menu> children;
}
