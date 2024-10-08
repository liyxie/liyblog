package com.liy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liy.entity.Menu;
import com.liy.entity.UserRole;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 系统管理 - 用户角色关联表  Mapper 接口
 * </p>
 *
 * @author blue
 * @since 2021-07-30
 */
@Repository
public interface UserRoleMapper extends BaseMapper<UserRole> {

    List<Menu> selectMenuByUserId(Long id);
}
