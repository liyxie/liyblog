package com.liy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liy.common.ResponseResult;
import com.liy.entity.Calendar;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 日历记事表 服务类
 * </p>
 *
 * @author liy
 * @since 2024-08-10
 */
public interface CalendarService extends IService<Calendar> {

    /**
     * 日历列表
     * @param title 标题
     * @return
     */
    ResponseResult selectCalendarPage(String title);

    /**
     * 添加日历事件
     * @param calendar 日历对象
     * @return
     */
    ResponseResult addCalendar(Calendar calendar);

    /**
     * 修改日历事件
     * @param calendar 日历对象
     * @return
     */
    ResponseResult updateCalendar(Calendar calendar);

    /**
     * 批量删除日历事件
     * @param list 日历ID集合
     * @return
     */
    ResponseResult deleteCalendar(List<Long> list);

    /**
     * 获取日历详情
     * @param id 日历ID
     * @return
     */
    ResponseResult getCalendarById(Long id);

    /**
     * 按时间范围查询日历事件
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return
     */
    ResponseResult getCalendarByDateRange(Date startDate, Date endDate);

    /**
     * 获取当前用户的所有日历事件
     * @return
     */
    ResponseResult getUserCalendarList();
}