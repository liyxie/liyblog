package com.liy.config.satoken;

import cn.dev33.satoken.stp.StpInterface;
import com.liy.mapper.MenuMapper;
import com.liy.mapper.RoleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * author: quequnlong
 * created: 2024/5/16 8:58
 * description: 自定义权限验证接口扩展
 */
@Component
@RequiredArgsConstructor
public class StpInterfaceImpl implements StpInterface {

    private final MenuMapper menuMapper;

    private final RoleMapper roleMapper;


    /**
     * 返回一个账号所拥有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        return menuMapper.getMenuByUserId(loginId);
    }

    /**
     * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        return roleMapper.selectByUserId(loginId); // 从数据库查询这个账号id拥有的角色列表
    }
}
