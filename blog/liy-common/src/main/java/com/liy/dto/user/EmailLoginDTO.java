package com.liy.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;

/**
 * @author blue
 * @date 2022/2/25
 * @apiNote
 */
@Data
@Schema(description = "邮箱登录信息")
public class EmailLoginDTO {
    /**
     *  邮箱
     */
    @NotBlank(message = "邮箱不能为空")
    @Schema(name = "email", description = "email", required = true, type = "String")
    private String email;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    @Schema(name = "password", description = "password", required = true, type = "String")
    private String password;

}
