package com.naown.shiro.realm;

import com.naown.shiro.entity.User;
import com.naown.utils.SaltUtils;
import com.naown.shiro.service.UserService;
import com.naown.utils.ShiroUtils;
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
 * @USER: chenjian
 * @DATE: 2021/2/17 1:10 周三
 **/
@Component
public class UserRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;
    /**
     * 授权
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("执行了授权");
        // 此处返回的就是在认证SimpleAuthenticationInfo方法中传的第一个参数
        User user = (User) principalCollection.getPrimaryPrincipal();
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        user.getRoles().forEach(role -> {
            simpleAuthorizationInfo.addRole(role.getDataScope());
        });
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
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        User user = userService.findByUserNameRole(token.getUsername());
        if(user == null) {
            throw new UnknownAccountException("账号或密码不正确");
        }
        /**
         * TODO 后续如果需要可以添加该功能
         *  账号锁定
         * 	if(user.getStatus() == 0){
         * 	    throw new LockedAccountException("账号已被锁定,请联系管理员");
         *  }
         */
        // 参数1：用户信息，一般为用户实体类 参数2: MD5+Salt 盐值加密的字符串 参数3: 随机盐 参数4: realm的名字 方法继承至父类
        return new SimpleAuthenticationInfo(user,user.getPassword(), new SaltUtils(user.getSalt()),this.getName());
    }

    /**
     * 重写父类的默认加密算法，改为MD5加密
     * 设置散列次数为16次，如果需要安全可言设置1024次或者2048
     * @param credentialsMatcher
     */
    @Override
    public void setCredentialsMatcher(CredentialsMatcher credentialsMatcher) {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName(ShiroUtils.HASH_ALGORITHM_NAME);
        hashedCredentialsMatcher.setHashIterations(ShiroUtils.HASH_ITERATIONS);
        super.setCredentialsMatcher(hashedCredentialsMatcher);
    }
}
