package com.liy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liy.common.ResponseResult;
import com.liy.dto.role.RoleMenuDTO;
import com.liy.entity.Role;

import java.util.List;

/**
 * <p>
 * 日志表 服务类
 * </p>
 *
 * @author blue
 * @since 2021-11-09
 */
public interface RoleService extends IService<Role> {

    /**
     * 分页
     * @param name
     * @return
     */
    ResponseResult selectRolePage(String name);

    /**
     * 添加
     * @param role
     * @return
     */
     ResponseResult addRole(Role role);

    /**
     * 修改
     * @param role
     * @return
     */
    ResponseResult updateRole(Role role);

    /**
     * 删除
     * @param ids
     * @return
     */
    ResponseResult deleteRole(List<Integer> ids);

    /**
     * 获取当前角色的菜单权限
     * @return
     */
    ResponseResult getCurrentUserRole();

    /**
     * 根据角色id查询当前所拥有的权限
     * @param roleId
     * @return
     */
    ResponseResult selectRoleMenuById(Integer roleId);

    /**
     * 分配权限
     * @param roleMenuDTO
     * @return
     */
    ResponseResult assignRoleMenus(RoleMenuDTO roleMenuDTO);
}
