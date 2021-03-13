package com.naown.common.service.impl;

import com.naown.common.entity.Menus;
import com.naown.common.mapper.MenusMapper;
import com.naown.common.service.MenusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: chenjian
 * @since: 2021/3/2 18:03 周二
 **/
@Service
public class MenusServiceImpl implements MenusService {

    @Autowired
    private MenusMapper menusMapper;

    /**
     * 获取所有的目录列表
     * @return
     */
    @Override
    public Menus[] listMenus() {
        return menusMapper.listMenus();
    }

    /**
     * 根据角色ID获取菜单列表
     * 并且在返回的菜单中筛选出有二级菜单的进行赋值
     * @param roleId
     * @return
     */
    @Override
    public List<Menus> listMenusByRoleId(Long roleId) {
        List<Menus> menus = menusMapper.listMenusByRoleId(roleId);
        List<Menus> newMenus = new ArrayList<>();
        for (Menus menu : menus){
            if (null == menu.getChildrenId()){
                Menus menuItem = getChildrenMenuList(menus,menu);
                newMenus.add(menuItem);
            }
        }
        return newMenus;
    }

    /**
     * 用来查询第一层树形结构的菜单
     * @param menus 所有菜单目录
     * @param menu 当前的菜单实体
     * @return
     */
    private Menus getChildrenMenuList(List<Menus> menus,Menus menu) {
        List<Menus> childrenMenu = new ArrayList<>();
        for (Menus menuItem : menus) {
            //找到父级相同的下级
            if (menuItem.getChildrenId() != null && menuItem.getChildrenId().equals(menu.getId())) {
                childrenMenu.add(menuItem);
            }
        }
        // 如果为空则不添加，因为前端那边设置了如果children不为空就显示一个下拉图标
        if (childrenMenu.size() > 0){
            menu.setChildren(childrenMenu);
        }
        // TODO 因为本项目菜单结构只会涉及到两层页面，如果三四层页面可以递归此方法 条件时childrenMenu.size()>0的时候
        return menu;
    }

    /**
     * 根据roleId查询所有有父级菜单的menu
     * @param roleId
     * @return
     */
    @Override
    public List<Menus> listChildrenByRoleId(Long roleId) {
        return null;
    }

}
