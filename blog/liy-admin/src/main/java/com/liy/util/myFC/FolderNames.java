package com.liy.util.myFC;
/**
 * @Author LiY
 */
import com.alibaba.excel.EasyExcel;

import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class FolderNames {

    static List<String> Mtrait = List.of(
            "酷", "长", "长发", "冷", "嫩", "双马尾", "幼", "罩", "泳衣", "死库水",
            "双飞", "短", "短发", "艳", "淫", "欲", "润", "可爱", "瘦", "jk", "三无", "巫女",
            "金", "黑", "黑长直", "拍差", "浴衣", "呆", "码", "口罩", "甜", "黄", "野外", "4K",
            "面码", "美", "漂亮", "白", "遮", "眼码", "cos", "大", "睡", "眼镜", "圣诞", "高",
            "羞", "敏", "声", "双机位", "学生", "4k", "秀", "小", "脸大", "身材", "没感情",
            "紫", "清秀", "熟女", "套", "熟", "圆脸", "制服", "3p", "三合一", "飞鸟酱", "旧",
            "道具", "二合一", "纯");



    public static void main(String[] args) {

        String[] directoryPaths = new String[]{
                "I:\\", "J:\\", "H:\\学习资料\\day01-SpringCloud01\\资料\\F", "G:\\新建文件夹", "E:\\杂\\新建文件夹"
        };

        String path = "I:\\";

        DFAUtil.init(Mtrait);

        List<fc> folderNames = new ArrayList<>();

        for (String s: directoryPaths) {
            folderNames.addAll(getFolderNames(s));
        }

        Pattern pattern = Pattern.compile("\\d{6,7}");

        List<fc> fc2 = folderNames.stream()
                // 过滤掉非F或f开头的文件夹名
                .filter(f -> f.getFullName().startsWith("FC") || f.getFullName().startsWith("fc"))
                // 将文件夹名替换为其弟2个"-"后的6位数字
                .map(fc -> {
                    Matcher matcher = pattern.matcher(fc.getFullName());
                    if (matcher.find()) {
                        int endIndex = matcher.end();
                        fc.setName(matcher.group());

                        Pattern pattern1 = Pattern.compile("^_(\\d+)");
                        String temp = fc.getFullName().substring(endIndex);
                        Matcher matcher1 = pattern1.matcher(temp);
                        if(matcher1.find()){
                            int endIndex1 = matcher1.end();
                            double score = Double.parseDouble(matcher1.group(1));
                            score = score > 10? score/10: score;
                            fc.setScore(score);

                            String trait = fc.getFullName().substring(endIndex).substring(endIndex1);

                            List<String> traits = getTraitsFoString(trait);
                            fc.setWord(traits.remove(traits.size() - 1));
                            fc.setTraits(traits);
                            fc.setTrait(String.join(", ", traits));
                        }
                    }
                    return fc;
                })
                .collect(Collectors.toList());

        toExcel(fc2, path);

    }

    private static List<String> getTraitsFoString(String trait) {
        List<String> traits = new ArrayList<>();
        Set<String> set = DFAUtil.getAndReplaceSensitiveWord(trait, "", DFAUtil.MaxMatchType);
        traits = new ArrayList<>(set);
        return traits;
    }

    public static void toExcel(List<fc> fcs, String path){
        String fileName = path + "simpleWrite" + System.currentTimeMillis() + ".xlsx";
        EasyExcel.write(fileName, fc.class)
                .sheet("模板")
                .doWrite(fcs);
    }

    public static List<fc> getFolderNames(String directoryPath) {
        List<fc> folderNames = new ArrayList<>();
        File directory = new File(directoryPath);
        if (directory.exists() && directory.isDirectory()) {
            getFolderNamesHelper(directory, folderNames);
        }
        return folderNames;
    }

    private static void getFolderNamesHelper(File directory, List<fc> folderNames) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    folderNames.add(new fc(file.getName(), file.getPath()));
                    getFolderNamesHelper(file, folderNames);
                }
            }
        }
    }

    private static void chuanjian(String filePath){
        // 指定文件路径
        File directory = new File(filePath);

        // 确保路径存在
        if (!directory.exists()) {
            System.out.println("指定的路径不存在！");
            return;
        }

        // 遍历目录中的文件
        File[] files = directory.listFiles();
        System.out.println(Arrays.toString(files));
        if (files != null) {
            for (File file : files) {
                // 创建新文件夹，命名为文件名（不含扩展名）
                String folderName = removeExtension(file.getName());
                File newFolder = new File(directory, folderName);

                // 创建文件夹
                if (!newFolder.exists()) {
                    if (!newFolder.mkdir()) {
                        System.out.println("无法创建文件夹: " + newFolder.getAbsolutePath());
                        continue;
                    }
                }

                // 将文件移动到新文件夹
                File newFile = new File(newFolder, file.getName());
                if (file.renameTo(newFile)) {
                    System.out.println("文件 " + file.getName() + " 已移动到 " + newFolder.getName() + " 文件夹中。");
                } else {
                    System.out.println("无法移动文件: " + file.getAbsolutePath());
                }
            }
        }
    }

    // 辅助方法：从文件名中删除扩展名
    private static String removeExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex == -1) {
            return fileName;
        }
        return fileName.substring(0, dotIndex);
    }
}
