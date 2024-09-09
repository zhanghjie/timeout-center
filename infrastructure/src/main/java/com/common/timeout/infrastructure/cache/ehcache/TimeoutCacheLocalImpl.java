package com.common.timeout.infrastructure.cache.ehcache;

import com.common.timeout.infrastructure.cache.TimeoutCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * TimeoutCacheLocalImpl
 * 功能描述：缓存服务本地缓存实现
 *
 * @author zhanghaojie
 * @date 2022/10/27 14:11
 */
@Service
@Slf4j
public class TimeoutCacheLocalImpl implements TimeoutCacheService {

    @Autowired
    private DefaultEhcacheUtils defaultEhcacheUtils;

    /**
     * 添加缓存 ehcache 不支持动态设置缓存过期时间
     *
     * @param key   redis的key
     * @param temp  缓存过期时间时间戳（秒级）默认-1不设置过期时间
     * @param param 保存的数据
     * @return Boolean
     * @author zhanghaojie
     * @date 2021/12/9 14:09
     */
    @Override
    public Boolean addValue(String key, Long temp, String param) {
        defaultEhcacheUtils.updateCache(key, param);
        return true;
    }

    /**
     * 添加缓存
     *
     * @param key   redis的key
     * @param param 保存的数据
     * @return Boolean
     * @author zhanghaojie
     * @date 2021/12/9 14:09
     */
    @Override
    public Boolean addValue(String key, String param) {
        defaultEhcacheUtils.updateCache(key, param);
        return true;
    }

    /**
     * 删除缓存内容
     *
     * @param key redis的key
     * @return Boolean
     * @author zhanghaojie
     * @date 2021/12/9 14:09
     */
    @Override
    public Boolean delVale(String key) {
        defaultEhcacheUtils.delCache(key);
        return null;
    }

    /**
     * 获取缓存
     *
     * @param key redis的key
     * @return Boolean
     * @author zhanghaojie
     * @date 2021/12/9 14:09
     */
    @Override
    public String getValue(String key) {
        Object value = defaultEhcacheUtils.getCache(key);
        if (Objects.isNull(value)) {
            return null;
        }
        return String.valueOf(value);
    }
}
