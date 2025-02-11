package com.liy.util.myFC;

import cn.idev.excel.annotation.ExcelIgnore;
import cn.idev.excel.annotation.ExcelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @Author LiY
 */
@Getter
@Setter
@EqualsAndHashCode
public class fc {
    @ExcelIgnore
    String fullName;
    @ExcelProperty("名")
    String name;
    @ExcelProperty("分")
    double score;
    @ExcelProperty("特")
    String trait;

    @ExcelIgnore
    List<String> traits;
    @ExcelProperty("路")
    String path;

    @ExcelProperty("额外")
    String word;

    fc(String fullName, String path){
        this.fullName = fullName;
        this.path = path;
    }
}
