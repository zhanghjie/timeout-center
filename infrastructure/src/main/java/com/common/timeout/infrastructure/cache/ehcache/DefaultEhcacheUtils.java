package com.common.timeout.infrastructure.cache.ehcache;

import lombok.extern.slf4j.Slf4j;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import javax.annotation.Resource;

/**
 * EhcacheUtils
 * 功能描述：DefaultEhcacheUtils
 *
 * @author zhanghaojie
 * @date 2022/3/17 16:09
 */
@Slf4j
public class DefaultEhcacheUtils {

    @Resource(name = "defaultEhcache")
    private static Cache cache;


    /**
     * 添加缓存数据
     *
     * @param key
     * @param value
     */
    public static void put(String key, Object value) {
        try {
            Element element = new Element(key, value);
            cache.put(element);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("添加缓存失败：{}", e.getMessage());
        }
    }

    /**
     * 获取缓存数据
     *
     * @param key
     * @return
     */
    public static Object get(String key) {
        try {
            Element element = cache.get(key);
            return element == null ? null : element.getObjectValue();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取缓存数据失败：{}", e.getMessage());
            return null;
        }
    }

    /**
     * 删除缓存数据
     *
     * @param key
     */
    public static void delete(String key) {
        try {
            cache.remove(key);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("删除缓存数据失败：{}", e.getMessage());
        }
    }


}
