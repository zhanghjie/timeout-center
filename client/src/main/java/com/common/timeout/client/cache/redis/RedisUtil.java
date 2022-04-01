package com.common.timeout.client.cache.redis;

import lombok.Data;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * RedisUtil
 * 功能描述：TODO
 *
 * @author zhanghaojie
 * @date 2022/1/13 14:39
 */
@Data
@Component("redisUtil")
public class RedisUtil {

    private RedisTemplate<String, Object> redisTemplate;
}
