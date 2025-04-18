package com.liy.vo.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author blue
 * @description: 系统用户出参
 * @date 2021/7/30 17:21
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SystemUserVO implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    @ApiModelProperty(value = "账号")
    private String username;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "头像")
    private String avatar;

    @ApiModelProperty(value = "个人简介")
    private String intro;

    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty(value = "角色Id")
    private Integer roleId;

    @ApiModelProperty(value = "权限集合")
    private List<String> perms;
}
