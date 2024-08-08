package com.liy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liy.common.ResponseResult;
import com.liy.entity.UserLog;

import java.util.List;

/**
 * <p>
 * 日志表 服务类
 * </p>
 *
 * @author blue
 * @since 2021-11-09
 */
public interface UserLogService extends IService<UserLog> {

    /**
     * 分页
     * @return
     */
    ResponseResult selectUserLogPage();

    /**
     * 删除
     * @param ids
     * @return
     */
    ResponseResult deleteUserLog(List<Long> ids);
}
