package com.liy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liy.entity.JobLog;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 定时任务调度日志表 Mapper 接口
 * </p>
 *
 * @author blue
 * @since 2021-12-08
 */
@Repository
public interface JobLogMapper extends BaseMapper<JobLog> {

    void clean();

}
