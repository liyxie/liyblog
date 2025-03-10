package com.liy.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.liy.utils.DateUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value="Menu对象", description="系统管理-权限资源表 ")
public class Menu implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键")
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "上级资源ID")
    private Integer parentId;

    @ApiModelProperty(value = "路由路径")
    private String path;

    @ApiModelProperty(value = "路由重定向路径")
    private String redirect;

    @ApiModelProperty(value = "组件路径")
    private String component;

    @ApiModelProperty(value = "菜单名称")
    private String title;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "资源图标")
    private String icon;

    @ApiModelProperty(value = "类型 menu、button")
    private String type;

    @ApiModelProperty(value = "资源名字")
    private String name;

    @ApiModelProperty(value = "权限标识")
    private String perm;

    @ApiModelProperty(value = "是否显示")
    private Integer hidden;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = DateUtil.FORMAT_STRING,timezone="GMT+8")
    private Date createdTime;

    @ApiModelProperty(value = "最后更新时间")
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = DateUtil.FORMAT_STRING,timezone="GMT+8")
    private Date updateTime;

    @TableField(exist = false)
    private List<Menu> children;
}
