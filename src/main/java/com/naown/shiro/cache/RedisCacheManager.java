package com.naown.shiro.cache;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;

/**
 * Shiro缓存管理器
 * 暂未使用到
 * @author : chenjian
 * @DATE: 2021/2/21 23:14 周日
 **/
public class RedisCacheManager implements CacheManager {
    @Override
    public <K, V> Cache<K, V> getCache(String cacheName) throws CacheException {
        return new RedisCache<>();
    }
}
