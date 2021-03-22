package com.naown.common.service;

import com.naown.common.entity.Menus;

import java.util.List;

/**
 * @author: chenjian
 * @since: 2021/3/2 18:02 周二
 **/
public interface MenusService {

    /**
     * 根据角色ID获取菜单列表
     * @param roleId
     * @return
     */
    List<Menus> listMenusByRoleId(Long roleId);

    /**
     * 根据roleId查询所有有父级菜单的menu
     * @param roleId
     * @return
     */
    List<Menus> listChildrenByRoleId(Long roleId);
}
