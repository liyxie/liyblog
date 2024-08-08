package com.liy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liy.entity.Sign;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * </p>
 *
 * @author blue
 * @since 2021-08-18
 */
@Repository
public interface SignMapper extends BaseMapper<Sign> {

    /**
     * 根据用户id和时间查询签到记录
     * @param userId 用户id
     * @param time 时间
     * @return
     */
    Sign selctSignByUserIdAndTime(@Param("userId") String userId, @Param("time")String time);

    /**
     * 用户的签到记录
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    List<String> getSignRecordsByUserId(@Param("startTime") String startTime, @Param("endTime")String endTime, @Param("userId")String userId);

    /**
     * 验证用户当日是否签到
     * @return
     */
    Sign validateTodayIsSign(@Param("time") String time, @Param("userId") String userId);
}
