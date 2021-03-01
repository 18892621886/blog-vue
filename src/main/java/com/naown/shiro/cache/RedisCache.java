package com.naown.shiro.cache;

import com.naown.utils.SpringContextUtils;
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
 * 自定义Shiro Redis缓存
 * @USER: chenjian
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

    @Override
    public V get(K k) throws CacheException {
        /**
         * 获取当前缓存的名字的K
         */
        return (V) this.getRedisTemplate().opsForHash().get(this.cacheName,k.toString());
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
        RedisTemplate redisTemplate = (RedisTemplate) SpringContextUtils.getBean("redisTemplate");
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        return redisTemplate;
    }
}
