package com.liy.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liy.common.Constants;
import com.liy.common.ResponseResult;
import com.liy.dto.role.RoleMenuDTO;
import com.liy.entity.Role;
import com.liy.mapper.RoleMapper;
import com.liy.service.RoleService;
import com.liy.utils.PageUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {


    /**
     * 角色列表
     *
     * @param name
     * @return
     */
    @Override
    public ResponseResult selectRolePage(String name) {
        Page<Role> data = baseMapper.selectPage(new Page<>(PageUtil.getPageNo(), PageUtil.getPageSize()), new LambdaQueryWrapper<Role>()
                .like(StringUtils.isNotBlank(name), Role::getName, name));
        return ResponseResult.success(data);
    }

    /**
     * 添加角色
     *
     * @param role
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult addRole(Role role) {
        baseMapper.insert(role);
        return ResponseResult.success();
    }

    /**
     * 修改角色
     *
     * @param role
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult updateRole(Role role) {
        baseMapper.updateById(role);
        return ResponseResult.success();
    }

    /**
     * 删除角色
     *
     * @param
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult deleteRole(List<Integer> ids) {
        baseMapper.deleteByIds(ids);
        ids.forEach(id -> baseMapper.delByRoleId(id, null));
        return ResponseResult.success();
    }

    /**
     * 获取当前登录用户所拥有的权限
     *
     * @param
     * @return
     */
    @Override
    public ResponseResult getCurrentUserRole() {
        Integer roleId = baseMapper.queryByUserId(StpUtil.getLoginIdAsString());
        List<Integer> list = baseMapper.queryByRoleMenu(roleId);
        return ResponseResult.success(list);
    }

    /**
     * 获取该角色所有的权限
     *
     * @param
     * @return
     */
    @Override
    public ResponseResult selectRoleMenuById(Integer roleId) {
        Role role = baseMapper.selectById(roleId);
        if (role.getCode().equals(Constants.ADMIN_CODE)) {
            return ResponseResult.success(baseMapper.selectAllMenuId());
        }
        List<Integer> list = baseMapper.queryByRoleMenu(roleId);
        return ResponseResult.success(list);
    }

    @Override
    public ResponseResult assignRoleMenus(RoleMenuDTO roleMenuDTO) {
        Role role = baseMapper.selectById(roleMenuDTO.getRoleId());
        if (role.getCode().equals(Constants.ADMIN_CODE)) {
            return ResponseResult.success();
        }
        //先删所有权限在新增
        baseMapper.delByRoleId(roleMenuDTO.getRoleId(), null);
        if (roleMenuDTO.getMenuIds().size() > 0) {
            baseMapper.insertBatchByRole(roleMenuDTO.getMenuIds(), roleMenuDTO.getRoleId());
        }
        return ResponseResult.success();
    }
}
