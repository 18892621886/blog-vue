package com.naown.common.service;

import com.naown.common.entity.Menus;

/**
 * @author: chenjian
 * @since: 2021/3/2 18:02 周二
 **/
public interface MenusService {
    /**
     * 获取所有的目录列表
     * @return
     */
    Menus[] listMenus();
}
