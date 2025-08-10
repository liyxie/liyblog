package com.liy.vo.calendar;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * <p>
 * 日历列表VO
 * </p>
 *
 * @author liy
 * @since 2024-08-10
 */
@Data
public class CalendarListVO {
    
    /**
     * 主键
     */
    private Long id;

    /**
     * 创建人id
     */
    private String creatorId;

    /**
     * 标题
     */
    private String title;

    /**
     * 开始时间
     */
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

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}