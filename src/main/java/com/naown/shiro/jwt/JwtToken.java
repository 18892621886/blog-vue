package com.naown.shiro.jwt;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author: chenjian
 * @since: 2021/3/12 21:20 周五
 **/
public class JwtToken implements AuthenticationToken{

    /** 密钥 */
    private final String token;

    public JwtToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return this.token;
    }

    @Override
    public Object getCredentials() {
        return this.token;
    }
}
