package com.liy.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 系统管理 - 角色-权限资源关联表 
 * </p>
 *
 * @author blue
 * @since 2021-07-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("b_role_menu")
@Schema(title="RoleMenu对象", description="系统管理 - 角色-权限资源关联表 ")
public class RoleMenu implements Serializable {

    private static final long serialVersionUID=1L;

    @Schema(description = "主键")
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "角色ID")
    private Integer roleId;

    @Schema(description = "菜单ID")
    private Integer menuId;

    @Schema(description = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createdTime;

    @Schema(description = "最后更新时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date lastTime;


}
