package com.liy.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;

@Data
public class EmailForgetPasswordDTO {

    @NotBlank(message = "邮箱不能为空")
    @Schema(name = "email", description = "email", required = true, type = "String")
    private String email;

    @NotBlank(message = "密码不能为空")
    @Schema(name = "password", description = "password", required = true, type = "String")
    private String password;

    @NotBlank(message = "验证码不能为空")
    private String code;
}
