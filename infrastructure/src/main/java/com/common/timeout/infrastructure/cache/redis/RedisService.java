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
                    "local val = redis.call('get', key);\n" +
                    "if val == false then\n" +
                    "   return nil\n" +
                    "end\n" +
                    "redis.call('del', key);\n" +
                    "return val";

    private static final String ADD_QUEUE_LUA =
            "local key = KEYS[1] \n" +
                    "local value = ARGV[1] \n" +
                    "local result = redis.call('set', key, value) \n" +
                    "if result then \n" +
                    "   return 1 \n" +
                    "else \n" +
                    "   return 0 \n" +
                    "end";

    private static final String ADD_TO_LIST_LUA =
            "local key = KEYS[1] \n" +
                    "local value = ARGV[1] \n" +
                    "local result = redis.call('lpush', key, value) \n" +
                    "return result";

    private static final String POP_FROM_LIST_LUA =
            "local key = KEYS[1] \n" +
                    "local result = redis.call('rpop', key) \n" +
                    "return result";

    private static final String ADD_TO_SORTED_SET_LUA =
            "local key = KEYS[1] \n" +
                    "local score = ARGV[1] \n" +
                    "local value = ARGV[2] \n" +
                    "local result = redis.call('zadd', key, score, value) \n" +
                    "return result";

    private static final String GET_FROM_SORTED_SET_LUA =
            "local key = KEYS[1] \n" +
                    "local maxScore = ARGV[1] \n" +
                    "local result = redis.call('zrangebyscore', key, 0, maxScore, 'limit', 0, 1) \n" +
                    "if #result > 0 then \n" +
                    "   redis.call('zrem', key, result[1]) \n" +
                    "   return result[1] \n" +
                    "else \n" +
                    "   return nil \n" +
                    "end";


    /**
     * 获取队列值并删除
     *
     * @param key 键
     * @return 值
     */
    public String getQueueAndDel(String key) {
        try {
            return redisTemplate.execute(RedisScript.of(GET_QUEUE_AND_DEL_LUA, String.class), Lists.newArrayList(key));
        } catch (Exception e) {
            log.error("获取redis value失败，key:{}, exception:{}", key, e.getMessage());
            return null;
        }
    }

    /**
     * 添加到队列
     *
     * @param key   键
     * @param value 值
     * @return 是否成功
     */
    public Boolean addToQueue(String key, String value) {
        try {
            Integer result = redisTemplate.execute(RedisScript.of(ADD_QUEUE_LUA, Integer.class), 
                Lists.newArrayList(key), Lists.newArrayList(value));
            return Objects.equals(result, 1);
        } catch (Exception e) {
            log.error("添加redis value失败，key:{}, exception:{}", key, e.getMessage());
            return false;
        }
    }

    /**
     * 添加到列表头部
     *
     * @param key   键
     * @param value 值
     * @return 列表长度
     */
    public Long addToList(String key, String value) {
        try {
            return redisTemplate.execute(RedisScript.of(ADD_TO_LIST_LUA, Long.class), 
                Lists.newArrayList(key), Lists.newArrayList(value));
        } catch (Exception e) {
            log.error("添加到列表失败，key:{}, exception:{}", key, e.getMessage());
            return 0L;
        }
    }

    /**
     * 从列表尾部弹出
     *
     * @param key 键
     * @return 值
     */
    public String popFromList(String key) {
        try {
            return redisTemplate.execute(RedisScript.of(POP_FROM_LIST_LUA, String.class), Lists.newArrayList(key));
        } catch (Exception e) {
            log.error("从列表弹出失败，key:{}, exception:{}", key, e.getMessage());
            return null;
        }
    }

    /**
     * 添加到有序集合
     *
     * @param key   键
     * @param score 分数
     * @param value 值
     * @return 添加的元素数量
     */
    public Long addToSortedSet(String key, double score, String value) {
        try {
            return redisTemplate.execute(RedisScript.of(ADD_TO_SORTED_SET_LUA, Long.class), 
                Lists.newArrayList(key), Lists.newArrayList(String.valueOf(score), value));
        } catch (Exception e) {
            log.error("添加到有序集合失败，key:{}, exception:{}", key, e.getMessage());
            return 0L;
        }
    }

    /**
     * 从有序集合获取并删除指定分数范围内的元素
     *
     * @param key      键
     * @param maxScore 最大分数
     * @return 值
     */
    public String getFromSortedSet(String key, double maxScore) {
        try {
            return redisTemplate.execute(RedisScript.of(GET_FROM_SORTED_SET_LUA, String.class), 
                Lists.newArrayList(key), Lists.newArrayList(String.valueOf(maxScore)));
        } catch (Exception e) {
            log.error("从有序集合获取失败，key:{}, exception:{}", key, e.getMessage());
            return null;
        }
    }

}
