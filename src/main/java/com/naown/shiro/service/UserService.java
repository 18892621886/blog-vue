package com.naown.shiro.service;

import com.naown.shiro.entity.User;

/**
 * @author: chenjian
 * @DATE: 2021/2/23 22:42 周二
 **/
public interface UserService {

    /**
     * 根据用户名获取角色权限
     * @param username
     * @return 用户实体
     */
    User findByUserNameRole(String username);
}
