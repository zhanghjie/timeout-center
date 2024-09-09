//package com.common.timeout.infrastructure.cache.redis;
//
//
//import com.common.timeout.infrastructure.cache.TimeoutCacheService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
///**
// * TimeoutServiceImpl
// * 功能描述: 超时任务服务实现类
// * <p>
// * 使用方式建议模仿{@link }
// *
// * @author zhanghaojie
// * @date 2021/12/9 14:15
// */
//@Service
//@Slf4j
//public class TimeoutRedisServiceImpl implements TimeoutCacheService {
//
//    @Autowired
//    private RedisService redisService;
//
//    private static final String STORE_KEY = "_store_";
//
//    private static final String PREPARE_KEY = "_prepare_";
//
//    private static final String DEAD_KEY = "_store_";
//
//    /**
//     * 往主队列里添加数据
//     *
//     * @param key   redis的key
//     * @param param 保存的数据
//     * @return key的集合
//     * @author zhanghaojie
//     * @date 2021/12/9 14:09
//     */
//    @Override
//    public Boolean addToStoreQueue(String key, Long temp, String param) {
//        return redisService.addToQueue(key + STORE_KEY + temp, param);
//    }
//
//    /**
//     * 往已发送队列中添加数据
//     *
//     * @param key redis的key
//     * @return key的集合
//     * @author zhanghaojie
//     * @date 2021/12/9 14:09
//     */
//    @Override
//    public Boolean addToPrepareQueue(String key, Long temp, String param) {
//        return redisService.addToQueue(key + PREPARE_KEY + temp, param);
//    }
//
//    /**
//     * 往死信队列中添加数据
//     *
//     * @param key   redis的key
//     * @param param 保存的数据
//     * @return key的集合
//     * @author zhanghaojie
//     * @date 2021/12/9 14:09
//     */
//    @Override
//    public Boolean addToDeadQueue(String key, Long temp, String param) {
//        return redisService.addToQueue(key + DEAD_KEY + temp, param);
//    }
//
//    /**
//     * 从未发送中取值并删除
//     *
//     * @param key  redis的key
//     * @param temp 时间戳（秒级）
//     * @return key的集合
//     * @author zhanghaojie
//     * @date 2021/12/9 14:09
//     */
//    @Override
//    public List<String> getStoreQueue(String key, Long temp) {
//        return redisService.getQueueAndDel(key + STORE_KEY + temp);
//    }
//
//    /**
//     * 从已发送中取值并删除
//     *
//     * @param key  redis的key
//     * @param temp 时间戳（秒级）
//     * @return key的集合
//     * @author zhanghaojie
//     * @date 2021/12/9 14:09
//     */
//    @Override
//    public List<String> getPrepareQueue(String key, Long temp) {
//        return redisService.getQueueAndDel(key + PREPARE_KEY + temp);
//    }
//
//    /**
//     * 从死信队列中取值并删除
//     *
//     * @param key  redis的key
//     * @param temp 时间戳（秒级）
//     * @return key的集合
//     * @author zhanghaojie
//     * @date 2021/12/9 14:09
//     */
//    @Override
//    public List<String> getDeadQueue(String key, Long temp) {
//        return redisService.getQueueAndDel(key + DEAD_KEY + temp);
//    }
//
//}
