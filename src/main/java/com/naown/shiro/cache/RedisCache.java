package com.naown.shiro.cache;

import com.naown.utils.JwtUtils;
import com.naown.utils.RedisUtils;
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
     * 获取redis中的shiro缓存 报黄是因为没有指定泛型 hash的key为shiro:hash 内部key为shiro:cache:username
     * @param key
     * @return
     * @throws CacheException
     */
    @Override
    public Object get(Object key) throws CacheException {
        if (Boolean.FALSE.equals(this.getRedisTemplate().hasKey(this.getKey(key)))){
            return null;
        }
        return this.getRedisTemplate().opsForHash().get(Constant.PREFIX_SHIRO_HASH_CACHE,this.getKey(key));
    }

    @Override
    public Object put(Object key, Object value) throws CacheException {
        /**
         * 缓存cacheName 当做K 并且5小时后过期
         */
        this.getRedisTemplate().opsForHash().put(Constant.PREFIX_SHIRO_HASH_CACHE,this.getKey(key),value);
        this.getRedisTemplate().expire(Constant.PREFIX_SHIRO_HASH_CACHE,5,TimeUnit.HOURS);
        return null;
    }

    @Override
    public V remove(Object key) throws CacheException {
        return (V)getRedisTemplate().opsForHash().delete(Constant.PREFIX_SHIRO_HASH_CACHE,this.getKey(key));
    }

    @Override
    public void clear() throws CacheException {
        getRedisTemplate().delete(Constant.PREFIX_SHIRO_HASH_CACHE);
    }

    @Override
    public int size() {
        return getRedisTemplate().opsForHash().size(Constant.PREFIX_SHIRO_HASH_CACHE).intValue();
    }

    @Override
    public Set<K> keys() {
        return this.getRedisTemplate().keys(Constant.PREFIX_SHIRO_HASH_CACHE);
    }

    @Override
    public Collection<V> values() {
        return this.getRedisTemplate().opsForHash().values(Constant.PREFIX_SHIRO_HASH_CACHE);
    }

    private RedisTemplate getRedisTemplate(){
        return (RedisTemplate)SpringContextUtils.getBean("redisTemplate");
    }
}
