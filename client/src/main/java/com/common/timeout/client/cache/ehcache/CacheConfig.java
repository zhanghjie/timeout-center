package com.common.timeout.client.cache.ehcache;

import net.sf.ehcache.Cache;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import net.sf.ehcache.CacheManager;
import org.springframework.util.ClassUtils;

/**
 * CacheConfig
 * 功能描述：TODO
 *
 * @author zhanghaojie
 * @date 2022/3/17 15:52
 */
public class CacheConfig {


    /**
     * 给CacheHelper用的cacheManager
     *
     * @return
     */
    @Bean(name = "ehCacheManager")
    public CacheManager cacheManager() {
        final String path = ClassUtils.getDefaultClassLoader().getResource("").getPath() + "ehcache/ehcache.xml";
        CacheManager cacheManager = CacheManager.create(path);
        return cacheManager;
    }

    /**
     * 给DefaultEhcacheUtils用的Cache
     *
     * @return
     */
    @Bean(name = "defaultEhcache")
    public Cache defaultEhcache() {
        final String path = ClassUtils.getDefaultClassLoader().getResource("").getPath() + "ehcache/ehcache.xml";
        ApplicationContext app = new AnnotationConfigApplicationContext(CacheConfig.class);
        CacheManager cacheManager = (CacheManager) app.getBean("ehCacheManager");
        Cache cache = cacheManager.getCache("defaultCache");
        return cache;
    }

}
