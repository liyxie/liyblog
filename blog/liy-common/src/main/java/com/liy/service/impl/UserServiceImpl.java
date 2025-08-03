package com.liy.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liy.common.Constants;
import com.liy.common.RedisConstants;
import com.liy.common.ResponseResult;
import com.liy.config.satoken.OnlineUser;
import com.liy.dto.user.SystemUserDTO;
import com.liy.dto.user.UserPasswordDTO;
import com.liy.entity.Menu;
import com.liy.entity.User;
import com.liy.exception.BusinessException;
import com.liy.mapper.UserMapper;
import com.liy.service.MenuService;
import com.liy.service.RedisService;
import com.liy.service.UserService;
import com.liy.utils.AesEncryptUtil;
import com.liy.utils.BeanCopyUtil;
import com.liy.utils.PageUtil;
import com.liy.vo.menu.RouterVO;
import com.liy.vo.user.SystemUserInfoVO;
import com.liy.vo.user.SystemUserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.*;

import static com.liy.common.ResultCode.ERROR_USER_NOT_EXIST;

/**
 * @author blue
 * @description:
 * @date 2021/7/30 17:25
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final MenuService menuService;

    private final RedisService redisService;


    /**
     * 用户列表
     * @param username
     * @param loginType
     * @return
     */
    @Override
    public ResponseResult selectUserPage(String username, Integer loginType) {
        Page<SystemUserInfoVO> page = baseMapper.selectPageRecord(new Page<>(PageUtil.getPageNo(), PageUtil.getPageSize()),username,loginType);
        return ResponseResult.success(page);
    }

    /**
     * 用户详情
     * @param id
     * @return
     */
    @Override
    public ResponseResult selectUserById(String id) {
        SystemUserVO user = baseMapper.getById(id);
        return ResponseResult.success(user);
    }

    /**
     *  添加用户
     * @param dto
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult addUser(SystemUserDTO dto) {
        Long count = baseMapper.selectCount(new LambdaQueryWrapper<User>().eq(User::getUsername,dto.getUsername()));
        if (count > 0 ){
            throw new BusinessException("用户名已存在!");
        }
        User user = BeanCopyUtil.copyObject(dto,User.class);
        user.setPassword(AesEncryptUtil.aesEncrypt(user.getPassword()));
        baseMapper.insert(user);
        return ResponseResult.success(user);
    }

    /**
     *  修改用户
     * @param user
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult updateUser(User user) {
        baseMapper.updateById(user);
        return ResponseResult.success();
    }

    /**
     * 删除用户
     * @param ids
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult deleteUSer(List<String> ids) {
        baseMapper.deleteByIds(ids);
        return ResponseResult.success();
    }

    /**
     * 获取当前登录用户详情
     * @return
     */
    @Override
    public ResponseResult getCurrentUserInfo() {
        SystemUserVO user = baseMapper.getById(StpUtil.getLoginIdAsString());
        List<String> list = menuService.selectButtonPermissions(user.getId());
        user.setPerms(list);
        return ResponseResult.success(user);
    }

    /**
     * 获取当前登录用户所拥有的菜单权限
     * @return
     */
    @Override
    public ResponseResult getCurrentUserMenu() {
        List<Menu> menus;
        if (StpUtil.hasRole(Constants.ADMIN_CODE)) {
            menus = menuService.list();
        }else {
            List<Integer> menuIds = baseMapper.getMenuId(StpUtil.getLoginIdAsString());
            menus = menuService.listByIds(menuIds);
        }

        List<RouterVO> routerVOS = menuService.buildRouterTree(menus);
        return ResponseResult.success(routerVOS);
    }


    /**
     * 修改密码
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult updatePassword(UserPasswordDTO passwordDTO) {

        User user = baseMapper.selectById(StpUtil.getLoginIdAsString());
        if (ObjectUtils.isEmpty(user)) {
            throw new BusinessException(ERROR_USER_NOT_EXIST.getDesc());
        }

        boolean isValid = AesEncryptUtil.validate(user.getPassword(),passwordDTO.getOldPassword());
        if (!isValid) {
            throw new BusinessException("旧密码校验不通过");
        }

        String newPassword = AesEncryptUtil.aesEncrypt(passwordDTO.getNewPassword());
        user.setPassword(newPassword);
        baseMapper.updateById(user);
        return ResponseResult.success("修改成功");
    }

    /**
     * 在线用户
     * @param keywords
     * @return
     */
    @Override
    public ResponseResult listOnlineUsers(String keywords) {

        int pageNo = PageUtil.getPageNo().intValue();
        int pageSize = PageUtil.getPageSize().intValue();

        Collection<String> keys = redisService.keys(RedisConstants.LOGIN_TOKEN.concat( "*"));
        List<String> totalList = new ArrayList<>(keys);
        int fromIndex = (pageNo-1) * pageSize;
        int toIndex = totalList.size() - fromIndex > pageSize ? fromIndex + pageSize : totalList.size();
        List<String> onlineUserList = totalList.subList(fromIndex, toIndex);

        List<OnlineUser> resultList = new ArrayList<>();
        for (String key : onlineUserList) {
            Object userObj = redisService.getCacheObject(key);
            OnlineUser onlineUser = JSONUtil.toBean(userObj.toString(), OnlineUser.class);
            resultList.add(onlineUser);
        }
        Map<String,Object> map = new HashMap<>();
        map.put("total",totalList.size());
        map.put("records",resultList);
        return ResponseResult.success(map);
    }

    /**
     * 踢人下线
     * @param token
     * @return
     */
    @Override
    public ResponseResult kick(String token) {
        log.info("当前踢下线的用户token为:{}",token);
        StpUtil.logoutByTokenValue(token);
        redisService.deleteObject(RedisConstants.LOGIN_TOKEN.concat( token));
        return ResponseResult.success();
    }
}
