package com.liy.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liy.common.ResponseResult;
import com.liy.entity.Calendar;
import com.liy.mapper.CalendarMapper;
import com.liy.service.CalendarService;
import com.liy.vo.calendar.CalendarListVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 日历记事表 服务实现类
 * </p>
 *
 * @author liy
 * @since 2024-08-10
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CalendarServiceImpl extends ServiceImpl<CalendarMapper, Calendar> implements CalendarService {

    /**
     * 日历列表
     * @param title 标题
     * @return
     */
    @Override
    public ResponseResult selectCalendarPage(String title) {
        String creatorId = StpUtil.getLoginIdAsString();
        List<CalendarListVO> calendarList = baseMapper.selectCalendarList(creatorId, title);
        return ResponseResult.success(calendarList);
    }

    /**
     * 添加日历事件
     * @param calendar 日历对象
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult addCalendar(Calendar calendar) {
        calendar.setCreatorId(StpUtil.getLoginIdAsString());
        calendar.setCreateTime(new Date());
        calendar.setUpdateTime(new Date());
        calendar.setIsDelete(0);
        
        if (calendar.getDraggable() == null) calendar.setDraggable("1");
        if (calendar.getResizable() == null) calendar.setResizable("1");
        if (calendar.getDeletable() == null) calendar.setDeletable("1");
        if (calendar.getAllDay() == null) calendar.setAllDay("0");
        
        baseMapper.insert(calendar);
        return ResponseResult.success();
    }

    /**
     * 修改日历事件
     * @param calendar 日历对象
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult updateCalendar(Calendar calendar) {
        String creatorId = StpUtil.getLoginIdAsString();
        
        Calendar existCalendar = baseMapper.selectOne(
            new LambdaQueryWrapper<Calendar>()
                .eq(Calendar::getId, calendar.getId())
                .eq(Calendar::getCreatorId, creatorId)
                .eq(Calendar::getIsDelete, 0)
        );
        
        if (existCalendar == null) {
            return ResponseResult.error("日历事件不存在或无权限修改");
        }
        
        calendar.setUpdateTime(new Date());
        calendar.setCreatorId(creatorId);
        baseMapper.updateById(calendar);
        return ResponseResult.success();
    }

    /**
     * 批量删除日历事件
     * @param list 日历ID集合
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult deleteCalendar(List<Long> list) {
        String creatorId = StpUtil.getLoginIdAsString();
        
        for (Long id : list) {
            Calendar calendar = baseMapper.selectOne(
                new LambdaQueryWrapper<Calendar>()
                    .eq(Calendar::getId, id)
                    .eq(Calendar::getCreatorId, creatorId)
                    .eq(Calendar::getIsDelete, 0)
            );
            
            if (calendar != null) {
                calendar.setIsDelete(1);
                calendar.setUpdateTime(new Date());
                baseMapper.updateById(calendar);
            }
        }
        
        return ResponseResult.success();
    }

    /**
     * 获取日历详情
     * @param id 日历ID
     * @return
     */
    @Override
    public ResponseResult getCalendarById(Long id) {
        String creatorId = StpUtil.getLoginIdAsString();
        
        Calendar calendar = baseMapper.selectOne(
            new LambdaQueryWrapper<Calendar>()
                .eq(Calendar::getId, id)
                .eq(Calendar::getCreatorId, creatorId)
                .eq(Calendar::getIsDelete, 0)
        );
        
        if (calendar == null) {
            return ResponseResult.error("日历事件不存在");
        }
        
        return ResponseResult.success(calendar);
    }

    /**
     * 按时间范围查询日历事件
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return
     */
    @Override
    public ResponseResult getCalendarByDateRange(Date startDate, Date endDate) {
        String creatorId = StpUtil.getLoginIdAsString();
        List<CalendarListVO> calendarList = baseMapper.selectCalendarByDateRange(creatorId, startDate, endDate);
        return ResponseResult.success(calendarList);
    }

    /**
     * 获取当前用户的所有日历事件
     * @return
     */
    @Override
    public ResponseResult getUserCalendarList() {
        String creatorId = StpUtil.getLoginIdAsString();
        List<CalendarListVO> calendarList = baseMapper.selectUserCalendarList(creatorId);
        return ResponseResult.success(calendarList);
    }
}