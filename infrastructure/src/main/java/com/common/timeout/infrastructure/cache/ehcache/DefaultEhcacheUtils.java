package com.common.timeout.infrastructure.cache.ehcache;

import com.common.timeout.infrastructure.annotation.ToLog;
import lombok.extern.slf4j.Slf4j;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * EhcacheUtils
 * 功能描述：DefaultEhcacheUtils
 *
 * @author zhanghaojie
 * @date 2022/3/17 16:09
 */
@Slf4j
@SuppressWarnings("WeakerAccess")
@CacheConfig(cacheNames = {"lemonCache"})
@Component
public class DefaultEhcacheUtils {

    @Cacheable(key = "#keyName")
    public Object getCache(String keyName) {
        log.info("[ EHCACHE ] 正在缓存 ==> Key: {}", keyName);
        return null;
    }

    @CachePut(key = "#keyName")
    public void updateCache(String keyName, String t) {
        log.info("[ EHCACHE ]  正在保存 ==> Key: {},Value: {}", keyName, t);
    }

    @CacheEvict(key = "#keyName")
    public void delCache(String keyName) {
        log.info("[ EHCACHE ]  正在删除 ==> Key:{}", keyName);
    }


}
