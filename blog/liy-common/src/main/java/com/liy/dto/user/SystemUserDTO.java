package com.liy.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;

@Data
public class SystemUserDTO {

    @Schema(name = "id", description = "用户id", required = true, type = "Long")
    private Long id;

    @Schema(name = "avatar", description = "头像", required = true, type = "String")
    private String avatar;

    @NotBlank(message = "用户名不能为空")
    @Schema(name = "username", description = "用户名", required = true, type = "String")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Schema(name = "password", description = "密码", required = true, type = "String")
    private String password;

    @NotBlank(message = "昵称不能为空")
    @Schema(name = "nickname", description = "昵称", required = true, type = "String")
    private String nickname;

    @Schema(name = "status", description = "状态", required = true, type = "int")
    private int status;

    @Schema(name = "roleId", description = "角色", required = true, type = "int")
    private int roleId;
}
