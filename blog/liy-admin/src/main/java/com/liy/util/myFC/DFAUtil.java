
package com.liy.util.myFC;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author LiY
 * 敏感词过滤
 */


@Data
@Slf4j
@Component
public class DFAUtil {

    private String filePath;
    private static String SENSITIVE_WORD_PATH;


    /**
     * 敏感词匹配规则
     */
    public static final int MinMatchTYpe = 1;      //最小匹配规则，如：敏感词库["中国","中国人"]，语句："我是中国人"，匹配结果：我是[中国]人
    public static final int MaxMatchType = 2;      //最大匹配规则，如：敏感词库["中国","中国人"]，语句："我是中国人"，匹配结果：我是[中国人]

    /**
     * 敏感词集合
     */
    public static HashMap sensitiveWordMap;

    private static DFAUtil dfaUtil;

    /**
     * 初始化敏感词库，构建DFA算法模型
     */
    public static void init(List<String> str){
        Set<String> sensitiveWordSet = new HashSet<>();
        sensitiveWordSet = str.stream().filter(Objects::nonNull).collect(Collectors.toSet());
        log.info("加载{}个敏感字",sensitiveWordSet.size());
        initSensitiveWordMap(sensitiveWordSet);
    }
    /**
     * 初始化敏感词库，构建DFA算法模型
     *
     */
    private static void initSensitiveWordMap(Set<String> sensitiveWordSet) {
        //初始化敏感词容器，减少扩容操作
        sensitiveWordMap = new HashMap(sensitiveWordSet.size());
        String key;
        Map nowMap;
        Map<String, String> newWorMap;
        //迭代sensitiveWordSet
        Iterator<String> iterator = sensitiveWordSet.iterator();
        while (iterator.hasNext()) {
            //关键字
            key = iterator.next();
            nowMap = sensitiveWordMap;
            for (int i = 0; i < key.length(); i++) {
                //转换成char型
                char keyChar = key.charAt(i);
                //库中获取关键字
                Object wordMap = nowMap.get(keyChar);
                //如果存在该key，直接赋值，用于下一个循环获取
                if (wordMap != null) {
                    nowMap = (Map) wordMap;
                } else {
                    //不存在则，则构建一个map，同时将isEnd设置为0，因为他不是最后一个
                    newWorMap = new HashMap<>();
                    //不是最后一个
                    newWorMap.put("isEnd", "0");
                    nowMap.put(keyChar, newWorMap);
                    nowMap = newWorMap;
                }
                if (i == key.length() - 1) {
                    //最后一个
                    nowMap.put("isEnd", "1");
                }
            }
        }
    }


    /**
     * 判断文字是否包含敏感字符，最大数量
     * @param txt 字符串
     * @return true或false
     */
    public static boolean contains(String txt) {
        return contains(txt, MaxMatchType);
    }

    /**
     * 判断文字是否包含敏感字符
     * @param txt 字符串
     * @param matchType 匹配数量原则
     * @return true或false
     */
    public static boolean contains(String txt, int matchType) {
        boolean flag = false;
        for (int i = 0; i < txt.length(); i++) {
            int matchFlag = checkSensitiveWord(txt, i, matchType); //判断是否包含敏感字符
            if (matchFlag > 0) {    //大于0存在，返回true
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 获取文字中的敏感词，最大数量
     * @param txt 字符串
     * @return Set<String> 敏感词集合
     */
    public static Set<String> getSensitiveWord(String txt) {
        return getSensitiveWord(txt, MaxMatchType);
    }
    /**
     * 获取文字中的敏感词
     * @param txt 字符串
     * @param matchType 匹配数量原则
     * @return Set<String> 敏感词集合
     */
    public static Set<String> getSensitiveWord(String txt, int matchType) {
        Set<String> sensitiveWordList = new HashSet<>();

        for (int i = 0; i < txt.length(); i++) {
            //判断是否包含敏感字符
            int length = checkSensitiveWord(txt, i, matchType);
            if (length > 0) {//存在,加入list中
                sensitiveWordList.add(txt.substring(i, i + length));
                i = i + length - 1;//减1的原因，是因为for会自增
            }
        }

        return sensitiveWordList;
    }


    /**
     * 替换敏感字字符，最大数量原则
     * @param txt 字符串
     * @param replaceChar 替换字符
     * @return 替换后字符串
     */
    public static String replaceSensitiveWord(String txt, char replaceChar) {
        return replaceSensitiveWord(txt, replaceChar, MaxMatchType);
    }

    /**
     * 替换敏感字字符，最大数量原则
     * @param txt 字符串
     * @param replaceStr 替换字符串
     * @return 替换后字符串
     */
    public static String replaceSensitiveWord(String txt, String replaceStr) {
        return replaceSensitiveWord(txt, replaceStr, MaxMatchType);
    }
    /**
     * 替换敏感字字符
     *
     */
    public static String replaceSensitiveWord(String txt, char replaceChar, int matchType) {
        String resultTxt = txt;
        //获取所有的敏感词
        Set<String> set = getSensitiveWord(txt, matchType);
        Iterator<String> iterator = set.iterator();
        String word;
        String replaceString;
        while (iterator.hasNext()) {
            word = iterator.next();
            replaceString = getReplaceChars(replaceChar, word.length());
            resultTxt = resultTxt.replaceAll(word, replaceString);
        }
        return resultTxt;
    }
    /**
     * 替换敏感字字符
     *
     */
    public static String replaceSensitiveWord(String txt, String replaceStr, int matchType) {
        String resultTxt = txt;
        //获取所有的敏感词
        Set<String> set = getSensitiveWord(txt, matchType);
        Iterator<String> iterator = set.iterator();
        String word;
        while (iterator.hasNext()) {
            word = iterator.next();
            resultTxt = resultTxt.replaceAll(word, replaceStr);
        }
        return resultTxt;
    }

    /**
     * 获得字符串中的敏感词和替换后的字符串
     * @param txt 字符串
     * @param replaceStr 替换字符串
     * @param matchType 匹配数量原则
     * @return set，最后一位存储替换字符串
     */
    public static Set<String> getAndReplaceSensitiveWord(String txt, String replaceStr, int matchType){
        String resultTxt = txt;
        //获取所有的敏感词
        Set<String> set = new LinkedHashSet<>(getSensitiveWord(txt, matchType));
//        System.out.println(set);
        Iterator<String> iterator = set.iterator();
        String word;
        while (iterator.hasNext()) {
            word = iterator.next();
            resultTxt = resultTxt.replaceAll(word, replaceStr);
        }
        set.add(resultTxt);
//        System.out.println(set);
        return set;
    }
    /**
     * 获取替换字符串
     * 填充char到替换String长度
     */
    private static String getReplaceChars(char replaceChar, int length) {
        String resultReplace = String.valueOf(replaceChar);
        for (int i = 1; i < length; i++) {
            resultReplace += replaceChar;
        }

        return resultReplace;
    }


    /**
     * 检查文字中是否包含敏感字符，检查规则如下：
     * @param txt 字符串
     * @param beginIndex 起始下标
     * @param matchType 匹配数量原则
     * @return 敏感词长度
     */
    private static int checkSensitiveWord(String txt, int beginIndex, int matchType) {
        //敏感词结束标识位：用于敏感词只有1位的情况
        // 用于标识是否匹配到了敏感词的结尾（即是否检测到了敏感词的完整匹配）
        boolean flag = false;
        //匹配标识数默认为0
        int matchFlag = 0;
        char word;
        Map nowMap = sensitiveWordMap;
        for (int i = beginIndex; i < txt.length(); i++) {
            word = txt.charAt(i);
            if (Character.isWhitespace(word)) {
                continue;
            }
            //获取指定key
            nowMap = (Map) nowMap.get(word);
            if (nowMap != null) {//存在，则判断是否为最后一个
                //找到相应key，匹配标识+1
                matchFlag++;
                //如果为最后一个匹配规则,结束循环，返回匹配标识数
                if ("1".equals(nowMap.get("isEnd"))) {
                    //结束标志位为true
                    flag = true;
                    //最小规则，直接返回,最大规则还需继续查找
                    if (MinMatchTYpe == matchType) {
                        break;
                    }
                }
            }
            //不存在，直接返回
            else {
                break;
            }
        }
        if (matchFlag < 1 || !flag) {//长度必须大于等于1，为词
            matchFlag = 0;
        }
        return matchFlag;
    }


}
