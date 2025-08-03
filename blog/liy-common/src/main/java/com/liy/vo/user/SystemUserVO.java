package com.liy.vo.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "账号")
    private String username;

    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "个人简介")
    private String intro;

    @Schema(description = "状态")
    private Integer status;

    @Schema(description = "角色Id")
    private Integer roleId;

    @Schema(description = "权限集合")
    private List<String> perms;
}
