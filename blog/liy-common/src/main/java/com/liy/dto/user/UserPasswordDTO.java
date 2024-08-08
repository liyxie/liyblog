package com.liy.dto.user;

import lombok.Data;

@Data
public class UserPasswordDTO {

    /**
     * 旧密码
     */
    private String oldPassword;

    /**
     * 新密码
     */
    private String newPassword;
}
