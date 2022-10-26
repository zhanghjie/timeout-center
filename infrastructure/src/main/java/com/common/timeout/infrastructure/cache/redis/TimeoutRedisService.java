package com.common.timeout.infrastructure.cache.redis;

import java.util.List;

/**
 * TimeoutService
 * 功能描述: 超时任务服务
 *
 * @author zhanghaojie
 * @date 2021/12/9 14:03
 */
public interface TimeoutRedisService {

    /**
     * 往主队列里添加数据
     *
     * @param key   redis的key
     * @param temp  时间戳（秒级）
     * @param param 保存的数据
     * @return Boolean
     * @author zhanghaojie
     * @date 2021/12/9 14:09
     */
    Boolean addToStoreQueue(String key, Long temp, String param);

    /**
     * 往已发送队列中添加数据
     *
     * @param key   redis的key
     * @param temp  时间戳（秒级）
     * @param param 保存的数据
     * @return Boolean
     * @author zhanghaojie
     * @date 2021/12/9 14:09
     */
    Boolean addToPrepareQueue(String key, Long temp, String param);

    /**
     * 往死信队列中添加数据
     *
     * @param key   redis的key
     * @param temp  时间戳（秒级）
     * @param param 保存的数据
     * @return Boolean
     * @author zhanghaojie
     * @date 2021/12/9 14:09
     */
    Boolean addToDeadQueue(String key, Long temp, String param);

    /**
     * 从未发送中取值并删除
     *
     * @param key  redis的key
     * @param temp 时间戳（秒级）
     * @return value的集合
     * @author zhanghaojie
     * @date 2021/12/9 14:09
     */
    List<String> getStoreQueue(String key, Long temp);

    /**
     * 从已发送中取值并删除
     *
     * @param key  redis的key
     * @param temp 时间戳（秒级）
     * @return value的集合
     * @author zhanghaojie
     * @date 2021/12/9 14:09
     */
    List<String> getPrepareQueue(String key, Long temp);

    /**
     * 从死信队列中取值并删除
     *
     * @param key  redis的key
     * @param temp 时间戳（秒级）
     * @return value的集合
     * @author zhanghaojie
     * @date 2021/12/9 14:09
     */
    List<String> getDeadQueue(String key, Long temp);


}
