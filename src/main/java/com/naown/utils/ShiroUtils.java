package com.naown.utils;

import com.naown.shiro.entity.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.ShiroException;
import org.apache.shiro.subject.Subject;
import org.springframework.http.HttpStatus;

/**
 * @USER: chenjian
 * @DATE: 2021/2/22 16:56 周一
 **/
public class ShiroUtils {
    /**  加密算法 */
    public final static String HASH_ALGORITHM_NAME = "MD5";
    /**  循环次数 */
    public final static int HASH_ITERATIONS = 16;

    public static Subject getSubject(){
        return SecurityUtils.getSubject();
    }

    /**
     * 获取系统用户名称
     *
     * @return 系统用户名称
     */
    public static String getCurrentUsername() {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        if (user == null) {
            throw new ShiroException("当前登录状态过期");
        }
        if (user.getUsername()!=null) {

            return user.getUsername();
        }
        throw new ShiroException("找不到当前登录的信息");
    }
}
