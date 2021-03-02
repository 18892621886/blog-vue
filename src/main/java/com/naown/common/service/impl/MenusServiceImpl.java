package com.naown.common.service.impl;

import com.naown.common.entity.Menus;
import com.naown.common.mapper.MenusMapper;
import com.naown.common.service.MenusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
