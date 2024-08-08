package com.liy.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liy.common.Constants;
import com.liy.common.ResponseResult;
import com.liy.entity.Menu;
import com.liy.mapper.MenuMapper;
import com.liy.service.MenuService;
import com.liy.vo.menu.RouterVO;
import com.liy.vo.menu.MenuOptionsVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * <p>
 * 系统管理-权限资源表  服务实现类
 * </p>
 *
 * @author blue
 * @since 2021-09-24
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    /**
     * 获取菜单树
     * @param list
     * @return
     */
    @Override
    public ResponseResult selectMenuTreeList(List<Menu> list) {
        List<Menu> resultList = new ArrayList<>();
        for (Menu menu : list) {
            Integer parentId = menu.getParentId();
            if ( parentId == null || parentId == 0)
                resultList.add(menu);
        }
        for (Menu menu : resultList) {
            menu.setChildren(getMenTreeChild(menu.getId(),list));
        }
        resultList.sort(Comparator.comparingInt(Menu::getSort));
        return ResponseResult.success(resultList);
    }


    /**
     * 添加菜单
     * @param menu
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult addMenu(Menu menu) {
        if (menu.getType().equals("CATALOG")) {
            menu.setComponent("Layout");
        }
        baseMapper.insert(menu);
        return ResponseResult.success();
    }

    /**
     * 修改菜单
     * @param menu
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult updateMenu(Menu menu) {
        baseMapper.updateById(menu);
        return ResponseResult.success();
    }

    /**
     * 删除菜单
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult deleteMenu(Integer id) {
        baseMapper.deleteById(id);
        baseMapper.delete(new LambdaQueryWrapper<Menu>().eq(Menu::getParentId,id));
        return ResponseResult.success();
    }

    @Override
    public List<RouterVO> buildRouterTree(List<Menu> menus) {
        List<RouterVO> resultList = new ArrayList<>();
        for (Menu menu : menus) {
            Integer parentId = menu.getParentId();
            if ( parentId == null || parentId == 0) {
                RouterVO.MetaVO metaVO = new RouterVO.MetaVO(menu.getTitle(),menu.getIcon(),menu.getHidden());
                RouterVO build = RouterVO.builder().id(menu.getId()).path(menu.getPath()).redirect(menu.getRedirect()).name(menu.getName()).component(menu.getComponent())
                        .meta(metaVO).sort(menu.getSort()).build();
                resultList.add(build);
            }
        }
        resultList.sort(Comparator.comparingInt(RouterVO::getSort));

        for (RouterVO routerVO : resultList) {
            routerVO.setChildren(getRouterChild(routerVO.getId(),menus));
        }
        return resultList;
    }

    @Override
    public ResponseResult getMenuOptions() {

        List<Menu> list = baseMapper.selectList(null);
        List<MenuOptionsVO> resultList = new ArrayList<>();
        for (Menu menu : list) {
            Integer parentId = menu.getParentId();
            if ( parentId == null || parentId == 0) {
                MenuOptionsVO menuOptionsVO = new MenuOptionsVO(menu.getId(), menu.getTitle());
                resultList.add(menuOptionsVO);
            }
        }
        for (MenuOptionsVO menu : resultList) {
            menu.setChildren(getOptionsChild(menu.getValue(),list));
        }
        return ResponseResult.success(resultList);
    }

    @Override
    public List<String> selectButtonPermissions(String userId) {
        return baseMapper.selectButtonPermissions(userId, StpUtil.hasRole(Constants.ADMIN_CODE));
    }

    //----------------自定义方法开始------------

    /**
     * 获取路由菜单树
     * @param pid
     * @param menus
     * @return
     */
    private List<RouterVO> getRouterChild(Integer pid , List<Menu> menus){
        if (menus == null) {
            return Collections.emptyList();
        }
        Map<Integer, RouterVO> routerMap = new HashMap<>();
        for (Menu e: menus) {
            Integer parentId = e.getParentId();
            if(parentId != null && parentId.equals(pid)){
                // 子菜单的下级菜单
                RouterVO.MetaVO metaVO = new RouterVO.MetaVO(e.getTitle(),e.getIcon(),e.getHidden());
                RouterVO build = RouterVO.builder().id(e.getId()).path(e.getPath()).redirect(e.getRedirect()).name(e.getName()).component(e.getComponent())
                        .meta(metaVO).sort(e.getSort()).build();
                routerMap.put(e.getId(), build);
            }
        }

        List<RouterVO> childrens = new ArrayList<>(routerMap.values());
        childrens.sort(Comparator.comparingInt(RouterVO::getSort));

        for (RouterVO e : childrens) {
            e.setChildren(getRouterChild(e.getId(), menus));
        }

        return childrens.isEmpty() ? Collections.emptyList() : childrens;
    }

    /**
     * 分配角色权限-下拉菜单树
     * @param pid
     * @param menus
     * @return
     */
    private List<MenuOptionsVO> getOptionsChild(Integer pid , List<Menu> menus){
        if (menus == null) {
            return Collections.emptyList();
        }

        Map<Integer, MenuOptionsVO> optionsMap = new HashMap<>();
        for (Menu menu : menus) {
            Integer parentId = menu.getParentId();
            if (parentId != null && parentId.equals(pid)) {
                MenuOptionsVO menuOptionsVO = new MenuOptionsVO(menu.getId(), menu.getTitle());
                optionsMap.put(menu.getId(), menuOptionsVO);
            }
        }

        List<MenuOptionsVO> childrens = new ArrayList<>(optionsMap.values());

        for (MenuOptionsVO e : childrens) {
            e.setChildren(getOptionsChild(e.getValue(), menus));
        }

        return childrens.isEmpty() ? Collections.emptyList() : childrens;
    }

    /**
     * 菜单列表-菜单树
     * @param pid
     * @param menus
     * @return
     */
    private List<Menu> getMenTreeChild(Integer pid , List<Menu> menus){
        List<Menu> childrens = new ArrayList<>();
        for (Menu e: menus) {
            Integer parentId = e.getParentId();
            if(parentId != null && parentId.equals(pid)){
                // 子菜单的下级菜单
                childrens.add( e );
            }
        }
        // 把子菜单的子菜单再循环一遍
        for (Menu e: childrens) {
            // childrens
            e.setChildren(getMenTreeChild(e.getId(),menus));
        }
        //停下来的条件，如果 没有子元素了，则停下来
        if( childrens.size()==0 ){
            return null;
        }
        return childrens;
    }
}
