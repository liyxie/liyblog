package com.liy.dto.user;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

/**
 * @author blue
 * @date 2022/3/11
 * @apiNote
 */
@Data
public class LoginDTO {

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

    private Boolean rememberMe;

    @NotBlank(message = "验证码")
    private String captchaCode;


    @NotBlank(message = "验证码缓存key")
    private String captchaKey;

}
