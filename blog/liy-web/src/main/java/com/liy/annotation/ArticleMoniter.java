package com.liy.annotation;

import java.lang.annotation.*;

/**
 * @Author LiY
 * 文章监听，处理阅读量
 */


@Documented
@Target(ElementType.METHOD)//注解放置的目标位置即方法级别
@Retention(RetentionPolicy.RUNTIME)//注解在哪个阶段执行
public @interface ArticleMoniter {

    String value() default ""; // 操作模块

    String type() default "";  // 操作类型

    String desc() default "";  // 操作说明

}
