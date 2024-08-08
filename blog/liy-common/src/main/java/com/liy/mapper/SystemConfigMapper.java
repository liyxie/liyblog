package com.liy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liy.entity.SystemConfig;
import com.liy.vo.system.TableListVO;

/**
 * <p>
 * 系统配置表 Mapper 接口
 * </p>
 *
 * @author blue
 * @since 2021-11-25
 */
public interface SystemConfigMapper extends BaseMapper<SystemConfig> {

    Page<TableListVO> selectTables(Page<Object> objectPage);
}
