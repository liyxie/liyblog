package com.liy.utils;

import cn.dev33.satoken.secure.SaSecureUtil;

/**
 *  <p> 加密工具 </p>
 *
 * @description:
 * @author: blue
 * @date: 2019/10/13 0013 15:25
 */
public class AesEncryptUtil {

    final static String key = "liy1900";
    /**
     * 校验内容是否一致
     */
    public static boolean validate(String password, String input) {
        return password.equalsIgnoreCase(aesEncrypt(input));
    }

    /**
     * AES加密
     *
     * @param password：密码
     * @return
     */
    public static String aesEncrypt(String password){
        return SaSecureUtil.aesEncrypt(key, password);
    }


    public static boolean matches(String password, String input){
        return SaSecureUtil.aesDecrypt(key, password).equals(input);
    }

    public static void main(String[] args) {
        String i = aesEncrypt("123456");
        System.out.println(i);
        System.out.println(SaSecureUtil.aesDecrypt("liy1900","K29CzeYFWL8Skdn2qATp8g=="));
    }
}
