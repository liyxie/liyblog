package com.liy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liy.common.ResponseResult;
import com.liy.dto.user.SystemUserDTO;
import com.liy.dto.user.UserPasswordDTO;
import com.liy.entity.User;
import java.util.List;


/**
 *
 * @author blue
 * @date: 2021/7/30 17:17
 */
public interface UserService extends IService<User> {

    /**
     * 用户列表
     * @param username
     * @param loginType
     * @return
     */
    ResponseResult selectUserPage(String username, Integer loginType);

    /**
     * 用户详情
     * @param id
     * @return
     */
    ResponseResult selectUserById(String id);

    /**
     * 添加用户
     * @param user
     * @return
     */
    ResponseResult addUser(SystemUserDTO user);

    /**
     * 修改用户
     * @param user
     * @return
     */
    ResponseResult updateUser(User user);

    /**
     * 批量删除用户
     * @param ids
     * @return
     */
    ResponseResult deleteUSer(List<String> ids);

    /**
     * 获取当前用户
     * @return
     */
    ResponseResult getCurrentUserInfo();

    /**
     * 获取当前用户拥有的菜单
     * @return
     */
    ResponseResult getCurrentUserMenu();


    /**
     * 修改密码
     * @return
     */
    ResponseResult updatePassword(UserPasswordDTO userPasswordDTO);

    /**
     * 在线用户
     * @return
     */
    ResponseResult listOnlineUsers(String keywords);

    /**
     * 强制下线
     * @return
     */
    ResponseResult kick(String token);


}
