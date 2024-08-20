package com.liy.utils;

import com.liy.common.Constants;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串处理工具类
 *
 * @Author liy
 */

public class StringUtils {

    public static String replaceKeywords(String content, String keywords) {
        // 创建Pattern对象，并指定忽略大小写的标志
        Matcher matcher = Pattern.compile(keywords, Pattern.CASE_INSENSITIVE).matcher(content);
        // 查找匹配的内容
        while (matcher.find()) {
            // 打印找到的内容
            return content.replaceAll(matcher.group(), Constants.PRE_TAG + matcher.group() + Constants.POST_TAG);
        }
        return content;
    }

    public static String removeLabel(String content) {
        // 去除标签
        content = content.replaceAll("</?[^>]*>", "");
        if (content == null || content.length() == 0 || "".equals(content)){
            return content;
        }
        // 只保留部分内容
        int index = 0;
        for (int i = 0; i < 4; i++) {
            index = content.indexOf("\n", index + 1);
            if (index == -1){
                return content;
            }
        }
        //截取 字符串。得到结果
        content = content.substring(0, index);
        return content;
    }

    public static String splicingUrl(char delimiter, String ...str){
        StringBuilder url = new StringBuilder(str[0]);
        for (int i = 1; i < str.length; i++) {
            boolean currentEndsWithDelimiter = url.charAt(url.length() - 1) == delimiter;

            // 检查下一个字符串的开头是否是分隔符
            boolean nextStartsWithDelimiter = str[i].charAt(0) == delimiter;

            // 如果当前字符串末尾不是分隔符，且下一个字符串开头不是分隔符，则添加分隔符
            if (!currentEndsWithDelimiter && !nextStartsWithDelimiter) {
                url.append(delimiter);
            }

            // 添加下一个字符串到URL
            url.append(str[i]);
        }
        return url.toString();
    }

}
