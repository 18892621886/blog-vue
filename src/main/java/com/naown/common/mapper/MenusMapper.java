package com.naown.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.naown.common.entity.Menus;

import java.util.List;

/**
 * 菜单的Mapper
 * @author: chenjian
 * @since: 2021/3/2 15:14 周二
 **/
public interface MenusMapper extends BaseMapper<Menus> {
    /**
     * 获取所有的菜单
     * @return
     */
    Menus[] listMenus();

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
