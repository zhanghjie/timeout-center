package com.common.timeout.infrastructure.cache.redis;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * redis服务
 *
 * @author yangchao.ye
 * @create 2016年3月14日
 */
@Service
@Slf4j
public class RedisService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    private static final String GET_QUEUE_AND_DEL_LUA =
            "local key = KEYS[1] \n" +
                    "local val = redis.call('keys', key);\n" +
                    "if val == nil then\n" +
                    "   return val\n" +
                    "end\n" +
                    "redis.call('del', key)" +
                    "return val";


    private static final String ADD_QUEUE_LUA =
            "local val \n" +
                    "val = redis.call('keys', KEYS[1]);\n" +
                    "if val == nil then\n" +
                    "   val = {}\n" +
                    "end\n" +
                    "table.insert(KEYS[1], ARGV[1])\n" +
                    "redis.call('MSET', KEYS[1], val)" +
                    "return 1";


    /**
     * 获取队列值并删除
     *
     * @param key
     * @return
     * @author zhanghaojie
     */
    public List<String> getQueueAndDel(String key) {
        try {
            return redisTemplate.execute(RedisScript.of(GET_QUEUE_AND_DEL_LUA), Lists.newArrayList(key), null);
        } catch (Exception e) {
            log.error("获取redis value失效，key:{},exception :{}", key, e);
            return null;
        }
    }

    /**
     * 获取队列值并删除
     *
     * @param key
     * @return
     * @author zhanghaojie
     */
    public Boolean addToQueue(String key, String value) {
        try {
            Integer result = redisTemplate.execute(RedisScript.of(ADD_QUEUE_LUA), Lists.newArrayList(key), Lists.newArrayList(value));
            if (Objects.equals(result, 1)) {
                return true;
            }
            return false;
        } catch (Exception e) {
            log.error("添加redis value失效，key:{},exception :{}", key, e);
            return false;
        }
    }

}
