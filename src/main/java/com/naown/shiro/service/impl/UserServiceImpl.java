package com.naown.shiro.service.impl;

import com.naown.shiro.entity.User;
import com.naown.shiro.mapper.UserMapper;
import com.naown.shiro.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: chenjian
 * @DATE: 2021/2/23 22:42 周二
 **/
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User findByUserNameRole(String username) {
        return userMapper.findByUserNameRole(username);
    }
}
