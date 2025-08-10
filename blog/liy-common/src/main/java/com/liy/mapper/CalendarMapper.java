package com.liy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liy.entity.Calendar;
import com.liy.vo.calendar.CalendarListVO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 日历记事表 Mapper 接口
 * </p>
 *
 * @author liy
 * @since 2024-08-10
 */
public interface CalendarMapper extends BaseMapper<Calendar> {

    /**
     * 查询用户日历列表
     * @param creatorId 创建人ID
     * @param title 标题
     * @return
     */
    List<CalendarListVO> selectCalendarList(@Param("creatorId") String creatorId, @Param("title") String title);

    /**
     * 按时间范围查询日历事件
     * @param creatorId 创建人ID
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return
     */
    List<CalendarListVO> selectCalendarByDateRange(@Param("creatorId") String creatorId, 
                                                  @Param("startDate") Date startDate, 
                                                  @Param("endDate") Date endDate);

    /**
     * 查询用户的所有日历事件
     * @param creatorId 创建人ID
     * @return
     */
    List<CalendarListVO> selectUserCalendarList(@Param("creatorId") String creatorId);
}