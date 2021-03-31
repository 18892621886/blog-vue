package com.naown.shiro.jwt;

import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.naown.common.entity.Result;
import com.naown.shiro.exception.CustomException;
import com.naown.utils.JsonConvertUtil;
import com.naown.utils.JwtUtils;
import com.naown.utils.RedisUtils;
import com.naown.utils.common.Constant;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.http.HttpStatus;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 参考
 * @author: chenjian
 * @since: 2021/3/12 21:36 周五
 **/
@Slf4j
public class JwtFilter extends BasicHttpAuthenticationFilter {

    // TODO 目前加入了token刷新机制，还有个严重的问题是如果出现异常，前端暂时无感知，后续得进行修复和进行相关跳转

    /**
     * 检测Header里面是否包含Authorization字段，有就进行Token登录认证授权
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        // 拿到当前Header中Authorization的AccessToken(Shiro中getAuthzHeader方法已经实现)
        String token = this.getAuthzHeader(request);
        return token != null;
    }

    /**
     * 进行AccessToken登录认证授权
     */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        // 拿到当前Header中Authorization的AccessToken(Shiro中getAuthzHeader方法已经实现)
        String authorization = this.getAuthzHeader(request);
        JwtToken token = new JwtToken(authorization);
        // 提交给UserRealm进行认证，如果错误他会抛出异常并被捕获 如果没有抛出异常则代表登入成功，返回true
        this.getSubject(request, response).login(token);
        String account = JwtUtils.getClaim(authorization, "username");
        //检查是否需要更换token，需要则重新颁发
        this.refreshTokenIfNeed(account,authorization,response);
        return true;
    }

    /**
     * 检查是否需要,若需要则校验时间戳，刷新Token，并更新时间戳
     * @param account
     * @param authorization
     * @param response
     * @return
     */
    private boolean refreshTokenIfNeed(String account, String authorization, ServletResponse response) {
        long currentTimeMillis = System.currentTimeMillis();
        //检查刷新规则
        if (this.refreshCheck(authorization, currentTimeMillis)) {
            /**
             * redis-key-前缀-shiro:refresh_check
             */
            String lockName = "shiro:refresh_check:" + account;
            // 乐观锁实现 暂时去除
            //boolean b = syncCacheService.getLock(lockName, Constants.ExpireTime.ONE_MINUTE);
            //if (b) {
                //获取到锁
                String refreshTokenKey= "shiro:refresh_token:" + account;
                // 检查是否缓存了key
                if(RedisUtils.hasKey(refreshTokenKey)){
                    //检查redis中的时间戳与token的时间戳是否一致
                    String tokenTimeStamp = RedisUtils.get(refreshTokenKey).toString();
                    String tokenMillis= JwtUtils.getClaim(authorization,"currentTimeMillis");
                    if(!tokenMillis.equals(tokenTimeStamp)){
                        throw new TokenExpiredException(String.format("账户%s的令牌无效", account));
                    }
                }
                //时间戳一致，则颁发新的令牌

                log.info(String.format("为账户%s颁发新的令牌", account));
                String newCurrentTimeMillis = String.valueOf(currentTimeMillis);
                String newToken = JwtUtils.sign(account, newCurrentTimeMillis);
                //更新RefreshToken缓存的时间戳 五分钟有效期
                RedisUtils.set(refreshTokenKey, currentTimeMillis, 5*60);

                HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
                httpServletResponse.setHeader("Authorization", newToken);
                httpServletResponse.setHeader("Access-Control-Expose-Headers", "Authorization");
            //}
            //释放锁 暂时没加
            //syncCacheService.releaseLock(lockName);
        }
        return true;
    }

    /**
     * 检查是否需要更新Token
     * @param authorization
     * @param currentTimeMillis
     * @return
     */
    private boolean refreshCheck(String authorization, Long currentTimeMillis) {
        String tokenMillis = JwtUtils.getClaim(authorization, "currentTimeMillis");
        /** 60分钟更新一次token */
        if (currentTimeMillis - Long.parseLong(tokenMillis) > (60 * 60 * 1000L)) {
            return true;
        }
        return false;
    }

    /**
     * 这里我们详细说明下为什么最终返回的都是true，即允许访问
     * 例如我们提供一个地址 GET /article
     * 登入用户和游客看到的内容是不同的
     * 如果在这里返回了false，请求会被直接拦截，用户看不到任何东西
     * 所以我们在这里返回true，Controller中可以通过 subject.isAuthenticated() 来判断用户是否登入
     * 如果有些资源只有登入用户才能访问，我们只需要在方法上面加上 @RequiresAuthentication 注解即可
     * 但是这样做有一个缺点，就是不能够对GET,POST等请求进行分别过滤鉴权(因为我们重写了官方的方法)，但实际上对应用影响不大
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        // 查看当前Header中是否携带Authorization属性(Token)，有的话就进行登录认证授权
        if (this.isLoginAttempt(request, response)) {
            try {
                // 进行Shiro的登录UserRealm
                this.executeLogin(request, response);
                return true;
            } catch (Exception e) {
                // 认证出现异常，传递错误信息msg
                String msg = e.getMessage();
                // 获取应用异常(该Cause是导致抛出此throwable(异常)的throwable(异常))
                Throwable throwable = e.getCause();
                if (throwable instanceof SignatureVerificationException) {
                    // 该异常为JWT的AccessToken认证失败(Token或者密钥不正确)
                    msg = "Token或者密钥不正确(" + throwable.getMessage() + ")";
                } else if (throwable instanceof TokenExpiredException) {
                    // TODO 如果异常了就进行token刷新 感觉逻辑有点问题后续需要在改进
                    String token = (String) SecurityUtils.getSubject().getPrincipal();
                    String account = JwtUtils.getClaim(token, "username");
                    // 该异常为JWT的AccessToken已过期，判断RefreshToken未过期就进行AccessToken刷新
                    if (this.refreshTokenIfNeed(account,token, response)) {
                        return true;
                    } else {
                        msg = "Token已过期(" + throwable.getMessage() + ")";
                    }
                } else {
                    // 应用异常不为空
                    if (throwable != null) {
                        // 获取应用异常msg
                        msg = throwable.getMessage();
                    }
                }
                // Token认证失败直接返回Response信息
                this.response401(response, msg);
                return false;
            }
        } else {
            // 没有携带Token
            HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
            // 获取当前请求类型
            String httpMethod = httpServletRequest.getMethod();
            // 获取当前请求URI
            String requestURI = httpServletRequest.getRequestURI();
            log.info("当前请求 {} Authorization属性(Token)为空 请求类型 {}", requestURI, httpMethod);
            // mustLoginFlag = true 开启任何请求必须登录才可访问
            final Boolean mustLoginFlag = false;
            if (mustLoginFlag) {
                this.response401(response, "请先登录");
                return false;
            }
        }
        return true;
    }

    /**
     * TODO 改过 此处需要修改，因为目前token没有刷新机制  暂时废弃此方法
     * 此处为AccessToken刷新，进行判断RefreshToken是否过期，未过期就返回新的AccessToken且继续正常访问 常量
     */
    private boolean refreshToken(ServletRequest request, ServletResponse response) {
        // 拿到当前Header中Authorization的AccessToken(Shiro中getAuthzHeader方法已经实现)
        String token = this.getAuthzHeader(request);
        // 获取当前Token的帐号信息
        String account = JwtUtils.getClaim(token, "username");
        // 判断Redis中RefreshToken是否存在
        if (RedisUtils.hasKey(Constant.PREFIX_SHIRO_REFRESH_TOKEN+account)) {
            // Redis中RefreshToken还存在，获取RefreshToken的时间戳
            String currentTimeMillisRedis = RedisUtils.get(Constant.PREFIX_SHIRO_REFRESH_TOKEN+account).toString();
            // 获取当前AccessToken中的时间戳，与RefreshToken的时间戳对比，如果当前时间戳一致，进行AccessToken刷新
            if (JwtUtils.getClaim(token, Constant.CURRENT_TIME_MILLIS).equals(currentTimeMillisRedis)) {
                // 获取当前最新时间戳
                String currentTimeMillis = String.valueOf(System.currentTimeMillis());
                // 读取配置文件，获取refreshTokenExpireTime属性
                // PropertiesUtil.readProperties("config.properties"); 注
                // String refreshTokenExpireTime = PropertiesUtil.getProperty("refreshTokenExpireTime"); 直接先用值代替
                String refreshTokenExpireTime = "1800";
                // 设置RefreshToken中的时间戳为当前最新时间戳，且刷新过期时间重新为30分钟过期(配置文件可配置refreshTokenExpireTime属性)
                RedisUtils.set(Constant.PREFIX_SHIRO_REFRESH_TOKEN + account, currentTimeMillis, Integer.parseInt(refreshTokenExpireTime));
                // 刷新AccessToken，设置时间戳为当前最新时间戳
                token = JwtUtils.sign(account, currentTimeMillis);
                // 将新刷新的AccessToken再次进行Shiro的登录
                JwtToken jwtToken = new JwtToken(token);
                // 提交给UserRealm进行认证，如果错误他会抛出异常并被捕获，如果没有抛出异常则代表登入成功，返回true
                this.getSubject(request, response).login(jwtToken);
                // 最后将刷新的AccessToken存放在Response的Header中的Authorization字段返回
                HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
                httpServletResponse.setHeader("Authorization", token);
                httpServletResponse.setHeader("Access-Control-Expose-Headers", "Authorization");
                return true;
            }
        }
        return false;
    }

    /**
     * 对跨域提供支持
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        // 跨域已经在OriginFilter处全局配置
        return super.preHandle(request, response);
    }

    /**
     * 这里我们详细说明下为什么重写
     * 可以对比父类方法，只是将executeLogin方法调用去除了
     * 如果没有去除将会循环调用doGetAuthenticationInfo方法
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) {
        this.sendChallenge(request, response);
        return false;
    }


    /**
     * 无需转发，直接返回Response信息
     */
    private void response401(ServletResponse response, String msg) {
        HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
        httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json; charset=utf-8");
        try (PrintWriter out = httpServletResponse.getWriter()) {
            String data = JsonConvertUtil.objectToJson(Result.error(HttpStatus.UNAUTHORIZED.value(), "无权访问(Unauthorized):" + msg));
            out.append(data);
        } catch (IOException e) {
            log.error("直接返回Response信息出现IOException异常:{}", e.getMessage());
            throw new CustomException("直接返回Response信息出现IOException异常:" + e.getMessage());
        }
    }
}
