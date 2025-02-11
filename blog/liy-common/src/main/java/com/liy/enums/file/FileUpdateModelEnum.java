package com.liy.enums.file;

import lombok.Getter;

/**
 * 文件操作类型枚举
 */

@Getter
public enum FileUpdateModelEnum {
    /**
     * 本地上传
     */
    LOCAL(1, "本地", "localUploadStrategyImpl", "local-1"),
    /**
     * 七牛云上传
     */
    QIN(2, "七牛云", "qiNiuUploadStrategyImpl", "qin-1"),

    /**
     * 阿里云上传
     */
    ALI(4, "阿里云", "aliUploadStrategyImpl", "ali-1"),

    /**
     * 腾讯云
     */
    TX(3, "腾讯云", "tXUploadStrategyImpl", "tx-1");

    //创建构造函数
    FileUpdateModelEnum(int code, String desc, String strategy, String platform) {
        this.type = code;
        this.desc = desc;
        this.strategy = strategy;
        this.platform = platform;
    }


    /**
     * 上传方式
     */
    private final Integer type;
    /**
     * 描述
     */
    private final String desc;
    /**
     * 策略
     */
    private final String strategy;
    /**
     * Storage名称
     */
    private final String platform;

    /**
     * 获取策略
     *
     * @param type 模式
     * @return {@link String} 上传策略
     */
    public static FileUpdateModelEnum getEnumByType(int type) {
        for (FileUpdateModelEnum value : FileUpdateModelEnum.values()) {
            if (value.getType() == (type)) {
                return value;
            }
        }
        return null;
    }

    /**
     *  根据type获取Strategy
     * @param type id
     * @return Strategy
     */
    public static String getStrategyByType(int type) {
        FileUpdateModelEnum fileUpdateModelEnum = getEnumByType(type);
        return fileUpdateModelEnum == null ? null : fileUpdateModelEnum.getStrategy();
    }
}
