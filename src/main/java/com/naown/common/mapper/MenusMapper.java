package com.naown.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.naown.common.entity.Menus;

import java.util.List;

/**
 * @author: chenjian
 * @since: 2021/3/2 15:14 周二
 **/
public interface MenusMapper extends BaseMapper<Menus> {
    /**
     * 获取所有的菜单
     * @return
     */
    Menus[] listMenus();
}
