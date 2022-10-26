package com.common.timeout.infrastructure.cache.redis;

/**
 * Constants
 * 功能描述: redis key 常量类
 *
 * @author zhanghaojie
 * @date 2021/12/9 15:01
 */
public enum RedisKey {

    /**
     * 商城订单超时任务redisKey
     */
    MART_ORDER_TIMEOUT_TASK_KEY("mart_order_task", 30 * 60),

    /**
     * 酒店订单超时任务redisKey
     */
    HOTEL_ORDER_TIMEOUT_TASK_KEY("hotel_order_task", 30 * 60),

    /**
     * 超时中心 分布式锁key
     */
    TIMEOUT_REDIS_LOCK_KEY("LOCK", 60 * 3),

    ;


    private String prefix;
    private Integer second;

    private RedisKey(String prefix, Integer second) {
        this.prefix = prefix;
        this.second = second;
    }

    public String getPrefix() {
        return prefix;
    }

    public Integer getSecond() {
        return second;
    }
}
