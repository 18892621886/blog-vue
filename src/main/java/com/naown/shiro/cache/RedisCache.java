package com.naown.shiro.cache;

import com.naown.utils.JwtUtils;
import com.naown.utils.SpringContextUtils;
import com.naown.utils.common.Constant;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 暂未使用到
 * 自定义Shiro Redis缓存
 * @author : chenjian
 * @DATE: 2021/2/21 23:14 周日
 **/
public class RedisCache<K,V> implements Cache<K,V> {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    private String cacheName;

    public RedisCache(String cacheName){
        this.cacheName = cacheName;
    }

    public RedisCache() {
    }

    /**
     * 获取自定义的key 全称为shiro:cache:username
     * @param key
     * @return
     */
    private String getKey(Object key){
        return Constant.PREFIX_SHIRO_CACHE + JwtUtils.getClaim(key.toString(),Constant.ACCOUNT);
    }

    /**
     * 获取redis中的shiro缓存 报黄是因为没有指定泛型
     * @param key
     * @return
     * @throws CacheException
     */
    @Override
    public Object get(Object key) throws CacheException {
        if (Boolean.FALSE.equals(this.getRedisTemplate().hasKey(getKey(key)))){
            return null;
        }
        return this.getRedisTemplate().opsForValue().get(this.getKey(key));
    }

    @Override
    public V put(K k, V v) throws CacheException {
        /**
         * 缓存cacheName 当做K 并且5小时后过期
         */
        this.getRedisTemplate().opsForHash().put(this.cacheName,k.toString(),v);
        this.getRedisTemplate().expire(this.cacheName,5, TimeUnit.HOURS);
        return null;
    }

    @Override
    public V remove(K k) throws CacheException {
        return (V)getRedisTemplate().opsForHash().delete(this.cacheName,k.toString());
    }

    @Override
    public void clear() throws CacheException {
        getRedisTemplate().delete(this.cacheName);
    }

    @Override
    public int size() {
        return getRedisTemplate().opsForHash().size(this.cacheName).intValue();
    }

    @Override
    public Set<K> keys() {
        return this.getRedisTemplate().keys(this.cacheName);
    }

    @Override
    public Collection<V> values() {
        return this.getRedisTemplate().opsForHash().values(this.cacheName);
    }

    private RedisTemplate getRedisTemplate(){
        return (RedisTemplate)SpringContextUtils.getBean("redisTemplate");
    }
}
