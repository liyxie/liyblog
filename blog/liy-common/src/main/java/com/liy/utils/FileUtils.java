package com.liy.utils;

import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.util.ObjectUtil;
import com.liy.enums.file.FileTypeEnum;
import com.liy.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @Author LiY
 *
 * 文件处理工具类
 */
public class FileUtils {

    /**
     * @description: 根据文件名判断类型
     * @author: liy
     * @param:
     * @return:
     **/
    public static FileTypeEnum getByUrl(String Url) {
        if (StringUtils.isEmpty(Url)) {
            return null;
        }
        // 根据后缀判断
        String fileType;

        final Pattern pattern = Pattern.compile("\\S*[?]\\S*");
        Matcher matcher = pattern.matcher(Url);

        String[] spUrl = Url.split("/");
        int len = spUrl.length;
        String endUrl = spUrl[len - 1];
        if(matcher.find()) {
            String[] spEndUrl = endUrl.split("\\?");
            fileType = spEndUrl[0].split("\\.")[1];
        }
        fileType = endUrl.split("\\.")[1];

        FileTypeEnum typeEnum = getEnumByExtension(fileType);
        if(typeEnum != null){
            return typeEnum;
        }

        // 根据文件流判断
        try {
            fileType = FileTypeUtil.getType(new URL(Url).openStream());
            return getEnumByExtension(fileType);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过扩展名获取枚举类型
     *
     * @param extension 扩展名
     * @return 枚举类
     */
    public static FileTypeEnum getEnumByExtension(String extension) {
        if (StringUtils.isEmpty(extension)) {
            return null;
        }
        for (FileTypeEnum typesEnum : FileTypeEnum.values()) {
            if (extension.equalsIgnoreCase(typesEnum.getExtension())) {
                return typesEnum;
            }
        }
        return null;
    }

    /**
     * Content-Type常用对照
     * 根据后缀获取Mime
     *
     * @param fileType 扩展名
     * @return mime类型
     */
    public static String getContentType(String fileType) {
        FileTypeEnum mimeTypeEnum = getEnumByExtension(fileType);
        if (mimeTypeEnum != null) {
            return mimeTypeEnum.getMimeType();
        }
        return null;
    }

    /**
     * @description: 对比
     * @author: liy
     * @param:
     * @return:
     **/
    public static Boolean ContentType(FileTypeEnum Type, Integer type) {
        if (Type == null) {
            return false;
        }
        for (FileTypeEnum typesEnum : Arrays.stream(FileTypeEnum.values()).filter(mimeTypeEnum -> mimeTypeEnum.getCode().equals(type)).collect(Collectors.toList())) {
            if (Type.equals(typesEnum)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 根据 File 获取后缀/扩展名
     */
    public static String getExtension(File file){
        String extension = null;

        // 根据文件流判断
        try {
            extension = FileTypeUtil.getType(file);
            if(ObjectUtil.isNull(getEnumByExtension(extension))){
                throw new BusinessException();
            }
            return extension;
        } catch (Exception e) {
            throw new BusinessException("文件类型识别错误");
        }
    }
    public static String getExtension(InputStream inputStream) {
        String extension = null;

        // 根据文件流判断
        try {
            extension = FileTypeUtil.getType(inputStream);
            if(ObjectUtil.isNull(getEnumByExtension(extension))){
                throw new BusinessException();
            }
            return extension;
        } catch (Exception e) {
            throw new BusinessException("文件类型识别错误");
        }
    }

    /**
     * 拼接路径
     * @param delimiter 分隔符
     * @param str 参与拼接字符串
     * @return url
     */
//    public static String splicingUrl(char delimiter, String ...str){
//        StringBuilder url = new StringBuilder(str[0]);
//        for (int i = 1; i < str.length; i++) {
//            boolean currentEndsWithDelimiter = url.charAt(url.length() - 1) == delimiter;
//
//            // 检查下一个字符串的开头是否是分隔符
//            boolean nextStartsWithDelimiter = str[i].charAt(0) == delimiter;
//
//            // 如果当前字符串末尾不是分隔符，且下一个字符串开头不是分隔符，则添加分隔符
//            if (!currentEndsWithDelimiter && !nextStartsWithDelimiter) {
//                url.append(delimiter);
//            }
//
//            // 添加下一个字符串到URL
//            url.append(str[i]);
//        }
//        return url.toString();
//    }




}
