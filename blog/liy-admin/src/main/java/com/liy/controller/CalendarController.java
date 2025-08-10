package com.liy.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.liy.annotation.OperationLogger;
import com.liy.common.ResponseResult;
import com.liy.entity.Calendar;
import com.liy.service.CalendarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 日历记事表 前端控制器
 * </p>
 *
 * @author liy
 * @since 2024-08-10
 */
@RestController
@RequestMapping("/system/calendar")
@RequiredArgsConstructor
@Tag(name = "日历管理")
public class CalendarController {

    private final CalendarService calendarService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @Operation(summary = "日历列表")
    public ResponseResult selectCalendarPage(String title) {
        return calendarService.selectCalendarPage(title);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @SaCheckPermission("system:calendar:add")
    @OperationLogger(value = "新增日历事件")
    @Operation(summary = "新增日历事件")
    public ResponseResult addCalendar(@RequestBody Calendar calendar) {
        return calendarService.addCalendar(calendar);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    @SaCheckPermission("system:calendar:update")
    @Operation(summary = "修改日历事件")
    @OperationLogger(value = "修改日历事件")
    public ResponseResult updateCalendar(@RequestBody Calendar calendar) {
        return calendarService.updateCalendar(calendar);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    @SaCheckPermission("system:calendar:delete")
    @Operation(summary = "批量删除日历事件")
    @OperationLogger(value = "批量删除日历事件")
    public ResponseResult deleteCalendar(@RequestBody List<Long> list) {
        return calendarService.deleteCalendar(list);
    }

    @RequestMapping(value = "/info/{id}", method = RequestMethod.GET)
    @Operation(summary = "获取日历详情")
    public ResponseResult getCalendarById(@PathVariable Long id) {
        return calendarService.getCalendarById(id);
    }

    @RequestMapping(value = "/range", method = RequestMethod.GET)
    @Operation(summary = "按时间范围查询日历事件")
    public ResponseResult getCalendarByDateRange(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endDate) {
        return calendarService.getCalendarByDateRange(startDate, endDate);
    }

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    @Operation(summary = "获取当前用户的所有日历事件")
    public ResponseResult getUserCalendarList() {
        return calendarService.getUserCalendarList();
    }
}