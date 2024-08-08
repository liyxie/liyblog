package com.liy.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author blue
 * @date 2022/2/25
 * @apiNote
 */
@Data
@ApiModel(description = "邮箱注册信息")
public class EmailRegisterDTO {

    @NotBlank(message = "邮箱不能为空")
    @ApiModelProperty(name = "email", value = "email", required = true, dataType = "String")
    private String email;


    @NotBlank(message = "密码不能为空")
    @ApiModelProperty(name = "password", value = "password", required = true, dataType = "String")
    private String password;

    @NotBlank(message = "昵称不能为空")
    private String nickname;

    @NotBlank(message = "验证码不能为空")
    private String code;

}
