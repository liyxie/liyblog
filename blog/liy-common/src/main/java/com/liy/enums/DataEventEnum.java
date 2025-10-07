package com.liy.enums;


import lombok.Getter;

@Getter
public enum DataEventEnum {

    RESOURCE_ADD(3, "添加文件资源"),

    EMAIL_SEND(4, "发送邮件信息"),

    SYSTEM_NOTICE(5, "系统通知");

    //创建构造函数
    DataEventEnum(int code, String desc) {
        this.type = code;
        this.desc = desc;
    }


    /**
     * 发布类型
     */
    private final Integer type;
    /**
     * 发布描述
     */
    private final String desc;


}
