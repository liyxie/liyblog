package com.liy.enums.file;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author LiY
 *
 * 腾讯云存储地域类型
 */

@Slf4j
public enum TXAreaEnum {

    beijing1("ap-beijing-1", "北京一区"),
    beijing("ap-beijing", "北京"),
    nanjing("ap-nanjing", "南京"),
    shanghai("ap-shanghai", "上海"),
    guangzhou("ap-guangzhou", "广州"),
    chengdu("ap-chengdu", "成都"),
    chongqing("ap-chongqing", "重庆");

    TXAreaEnum(String code, String name){
        this.code = code;
        this.name = name;
    }

    private final String code;
    private final String name;

    public static String getRegion(String name) {
        for (TXAreaEnum value : TXAreaEnum.values()) {
            if (value.name.equals(name)) {
                return value.getCode();
            }
        }
        return null;
    }


    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }
}
