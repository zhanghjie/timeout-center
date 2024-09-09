package com.common.timeout.infrastructure.cache;

/**
 * TimeoutService
 * 功能描述: 超时任务服务
 *
 * @author zhanghaojie
 * @date 2021/12/9 14:03
 */
public interface TimeoutCacheService {

    /**
     * 添加缓存
     *
     * @param key   redis的key
     * @param temp  缓存过期时间时间戳（秒级）默认-1不设置过期时间
     * @param param 保存的数据
     * @return Boolean
     * @author zhanghaojie
     * @date 2021/12/9 14:09
     */
    Boolean addValue(String key, Long temp, String param);

    /**
     * 添加缓存
     *
     * @param key   redis的key
     * @param param 保存的数据
     * @return Boolean
     * @author zhanghaojie
     * @date 2021/12/9 14:09
     */
    Boolean addValue(String key, String param);

    /**
     * 删除缓存内容
     *
     * @param key redis的key
     * @return Boolean
     * @author zhanghaojie
     * @date 2021/12/9 14:09
     */
    Boolean delVale(String key);

    /**
     * 获取缓存
     *
     * @param key redis的key
     * @return Boolean
     * @author zhanghaojie
     * @date 2021/12/9 14:09
     */
    String getValue(String key);


}
