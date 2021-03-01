package com.naown.shiro.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.naown.shiro.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * UserMapper接口 用于数据交互
 * @USER: chenjian
 * @DATE: 2021/2/20 22:05 周六
 **/
public interface UserMapper extends BaseMapper<User> {
    User findByUserNameRole(String username);
}
