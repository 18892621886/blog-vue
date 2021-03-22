package com.naown.shiro.realm;

import com.naown.shiro.jwt.JwtToken;
import com.naown.shiro.entity.User;
import com.naown.utils.JwtUtils;
import com.naown.utils.SaltUtils;
import com.naown.shiro.service.UserService;
import com.naown.utils.ShiroUtils;
import com.naown.utils.common.Constant;
import io.netty.util.internal.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * 参考 https://github.com/dolyw/VueStudy/tree/master/VueStudy08-JWT/jwt-demo
 * @author : chenjian
 * @DATE: 2021/2/17 1:10 周三
 **/
@Component
public class UserRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    /**
     * 必须重写此方法，否则会报错
     * @param token
     * @return
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    /**
     * 只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        // account 用户名
        String account = JwtUtils.getClaim(principalCollection.toString(), "username");
        // UserDto userDto = new UserDto();
        // userDto.setAccount(account);
        // 查询用户角色
        User user = userService.findByUserNameRole(account);
        /**
         * 添加用户权限的需求在这边写
         */
        simpleAuthorizationInfo.addRole(user.getRoles().get(0).getName());

        return simpleAuthorizationInfo;
    }

    /**
     * 认证
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String token = (String) authenticationToken.getCredentials();
        // 解密获得account，用于和数据库进行对比
        String account = JwtUtils.getClaim(token, "username");
        // 帐号为空
        if (StringUtils.isBlank(account)) {
            throw new AuthenticationException("Token中帐号为空(The account in Token is empty.)");
        }
        // 查询用户是否存在
        //UserDto userDto = new UserDto();
        // TODO userDto.setAccount(account); 此处后续使用缓存实现，因为每一次都需要从数据库中查询
        User user = userService.findByUserNameRole(account);
        if (user == null) {
            throw new AuthenticationException("该帐号不存在(The account does not exist.)");
        }
        // 开始认证，要AccessToken认证通过，且Redis中存在RefreshToken，且两个Token时间戳一致
        // 暂时去除redis缓存&& JedisUtil.exists(Constant.PREFIX_SHIRO_REFRESH_TOKEN + account)
        if (JwtUtils.verify(token)) {
            // 获取RefreshToken的时间戳
            /*String currentTimeMillisRedis = JedisUtil.getObject(Constant.PREFIX_SHIRO_REFRESH_TOKEN + account).toString();*/
            // 获取AccessToken时间戳，与RefreshToken的时间戳对比
            /*if (JwtUtil.getClaim(token, Constant.CURRENT_TIME_MILLIS).equals(currentTimeMillisRedis)) {
                return new SimpleAuthenticationInfo(token, token, "userRealm");
            }*/

            // 这是暂时只做token校验 暂无其他逻辑
            return new SimpleAuthenticationInfo(token, token, this.getName());
        }
        throw new AuthenticationException("Token已过期(Token expired or incorrect.)");
        /*UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        User user = userService.findByUserNameRole(token.getUsername());
        if(user == null) {
            throw new UnknownAccountException("账号或密码不正确");
        }*/
        /**
         * TODO 后续如果需要可以添加该功能
         *  账号锁定
         * 	if(user.getStatus() == 0){
         * 	    throw new LockedAccountException("账号已被锁定,请联系管理员");
         *  }
         */
        // 参数1：用户信息，一般为用户实体类 参数2: MD5+Salt 盐值加密的字符串 参数3: 随机盐 参数4: realm的名字 方法继承至父类
        /*return new SimpleAuthenticationInfo(user,user.getPassword(), new SaltUtils(user.getSalt()),this.getName());*/
    }

    /**
     * 重写父类的默认加密算法，改为MD5加密 目前为JwtToken认证所以加密暂时不需要，如果需要实现MD5加密可以在登录时进行一些逻辑操作
     * 设置散列次数为16次，如果需要安全可设置1024次或者2048
     * @param credentialsMatcher
     */
    /*@Override
    public void setCredentialsMatcher(CredentialsMatcher credentialsMatcher) {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName(ShiroUtils.HASH_ALGORITHM_NAME);
        hashedCredentialsMatcher.setHashIterations(ShiroUtils.HASH_ITERATIONS);
        super.setCredentialsMatcher(hashedCredentialsMatcher);
    }*/
}
