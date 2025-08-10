package com.liy.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;

/**
 * <p>
 * 日历记事表 实体类
 * </p>
 *
 * @author liy
 * @since 2024-08-10
 */
@Data
@TableName(value = "b_calendar")
public class Calendar {
    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 创建人id
     */
    private String creatorId;

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

    /**
     * 是否删除
     */
    private Integer isDelete;

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
}