package com.liy.dto;

public class Config {
    //数据库中使用逻辑删除的字段名称
    public static String logicName = "deleted";
    public static String entityFolderName = "entity";
    public static String poFolderName = "po";
    public static String poSuffix = "PO";
    public static boolean poOnUpdateOverWrite = true;
    public static String voFolderName = "vo";
    public static String voSuffix = "VO";
    public static boolean voOnUpdateOverWrite = true;
    //以什么字符串开始就是枚举
    public static String typeStartString = "\n";
    //分割符号
    public static String typeSplitString;
    public static String mapperFolderName = "mapper";
    public static String mapperSuffix = "Mapper";
    public static boolean mapperXmlOnUpdateOverWrite = true;
    public static boolean mapperJavaOnUpdateOverWrite = true;
    public static String serviceFolderName = "service";
    public static boolean serviceOnUpdateOverWrite = false;
    public static String controllerFolderName = "controller";
    public static boolean controllerOnUpdateOverWrite = false;
}
