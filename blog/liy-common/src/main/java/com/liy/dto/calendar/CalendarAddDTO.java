package com.liy.dto.calendar;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Date;

/**
 * <p>
 * 日历添加DTO
 * </p>
 *
 * @author liy
 * @since 2024-08-10
 */
@Data
public class CalendarAddDTO {

    /**
     * 标题
     */
    @NotBlank(message = "标题不能为空")
    private String title;

    /**
     * 开始时间
     */
    @NotNull(message = "开始时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date start;

    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date end;

    /**
     * 内容
     */
    private String content;

    /**
     * 背景色
     */
    private String bgcolor;

    /**
     * 是否拖动
     */
    private String draggable;

    /**
     * 是否可调大小
     */
    private String resizable;

    /**
     * 是否允许删除
     */
    private String deletable;

    /**
     * 是否全天
     */
    private String allDay;

    /**
     * 事件是否重复以及其重复规则
     */
    private String recurring;

    /**
     * 样式
     */
    private String className;
}