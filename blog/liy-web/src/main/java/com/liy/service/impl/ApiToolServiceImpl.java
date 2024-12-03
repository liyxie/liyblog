package com.liy.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.liy.common.ResponseResult;
import com.liy.entity.Tool;
import com.liy.entity.User;
import com.liy.mapper.ToolMapper;
import com.liy.service.ApiToolService;
import com.liy.service.ApiUserService;
import com.liy.service.ToolService;
import com.liy.vo.tool.ToolMenuVo;
import com.liy.vo.user.SystemUserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * @Author LiY
 */

@Service
@RequiredArgsConstructor
public class ApiToolServiceImpl implements ApiToolService {

    private final ToolMapper toolMapper;

    private final ToolService toolService;

    private final ApiUserService userService;

    @Override
    public ResponseResult selectToolList() {
        //判断用户是否管理员
        if (StpUtil.isLogin()){
            String userId = StpUtil.getLoginIdAsString();
            if (userId != null){
                SystemUserVO user = userService.getUser(userId);
                if(user.getRoleId() == 1){
                    return selectToolListAdmin();
                }
            }
        }
        List<Tool> toolList = toolMapper.selectToolList();
        List<ToolMenuVo> toolMenuVos = toolService.doMenuTree(toolList);
        return ResponseResult.success(toolMenuVos);
    }

    private ResponseResult selectToolListAdmin() {
        List<Tool> toolList = toolMapper.selectAllToolList();
        List<ToolMenuVo> toolMenuVos = toolService.doMenuTree(toolList);
        return ResponseResult.success(toolMenuVos);
    }
}
