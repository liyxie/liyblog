package com.liy.vo.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 验证码
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VerifyCodeVO {

    private String captchaKey;

    private String captchaBase64;
}
