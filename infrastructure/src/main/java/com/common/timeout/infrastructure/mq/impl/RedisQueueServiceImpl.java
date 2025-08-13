package com.common.timeout.infrastructure.mq.impl;

import com.alibaba.fastjson.JSON;
import com.common.timeout.infrastructure.cache.redis.RedisService;
import com.common.timeout.infrastructure.db.model.TimeoutTaskDTO;
import com.common.timeout.infrastructure.mq.QueueOperationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * RedisQueueServiceImpl
 * 功能描述：基于Redis实现的队列服务，支持三种队列操作
 *
 * @author zhanghaojie
 * @date 2024/01/15
 */
@Slf4j
@Service("redisQueueServiceImpl")
public class RedisQueueServiceImpl implements QueueOperationService {
    
    @Autowired
    private RedisService redisService;
    
    /**
     * Store Queue 前缀 - 存储队列，用于临时存储任务
     */
    private static final String STORE_QUEUE_PREFIX = "timeout:store:";
    
    /**
     * Prepare Queue 前缀 - 准备队列，用于存储即将执行的任务
     */
    private static final String PREPARE_QUEUE_PREFIX = "timeout:prepare:";
    
    /**
     * Dead Queue 前缀 - 死信队列，用于存储失败的任务
     */
    private static final String DEAD_QUEUE_PREFIX = "timeout:dead:";
    
    /**
     * 功能描述: 往待执行队列添加任务
     *
     * @param timeoutTask 任务
     * @author zhanghaojie
     * @date 2024/01/15
     */
    @Override
    public void addTaskToStoreQueue(TimeoutTaskDTO timeoutTask) {
        try {
            String key = STORE_QUEUE_PREFIX + timeoutTask.getBizType() + ":" + timeoutTask.getBizId();
            String taskJson = JSON.toJSONString(timeoutTask);
            
            // 使用有序集合存储，以执行时间作为分数
            Long result = redisService.addToSortedSet(key, timeoutTask.getActionTime(), taskJson);
            
            if (result > 0) {
                log.info("任务已添加到存储队列: bizType={}, bizId={}, actionTime={}", 
                    timeoutTask.getBizType(), timeoutTask.getBizId(), timeoutTask.getActionTime());
            } else {
                log.warn("任务添加到存储队列失败: bizType={}, bizId={}", 
                    timeoutTask.getBizType(), timeoutTask.getBizId());
            }
        } catch (Exception e) {
            log.error("添加任务到存储队列异常: bizType={}, bizId={}", 
                timeoutTask.getBizType(), timeoutTask.getBizId(), e);
        }
    }
    
    /**
     * 功能描述: 往就绪队列添加任务
     *
     * @param timeoutTask 任务
     * @author zhanghaojie
     * @date 2024/01/15
     */
    @Override
    public void addTaskToPrepareQueue(TimeoutTaskDTO timeoutTask) {
        try {
            String key = PREPARE_QUEUE_PREFIX + timeoutTask.getBizType();
            String taskJson = JSON.toJSONString(timeoutTask);
            
            // 使用列表存储准备执行的任务，按优先级排序
            Long result = redisService.addToList(key, taskJson);
            
            if (result > 0) {
                log.info("任务已添加到准备队列: bizType={}, bizId={}", 
                    timeoutTask.getBizType(), timeoutTask.getBizId());
            } else {
                log.warn("任务添加到准备队列失败: bizType={}, bizId={}", 
                    timeoutTask.getBizType(), timeoutTask.getBizId());
            }
        } catch (Exception e) {
            log.error("添加任务到准备队列异常: bizType={}, bizId={}", 
                timeoutTask.getBizType(), timeoutTask.getBizId(), e);
        }
    }
    
    /**
     * 功能描述: 从就绪队列删除任务
     *
     * @author zhanghaojie
     * @date 2024/01/15
     */
    @Override
    public void deleteTaskFromPrepareQueue() {
        // 这个方法设计为从所有业务类型的准备队列中获取任务
        // 实际使用中可能需要传入具体的业务类型
        log.warn("deleteTaskFromPrepareQueue方法需要传入具体的业务类型参数");
    }
    
    /**
     * 从指定业务类型的准备队列中获取任务
     *
     * @param bizType 业务类型
     * @return 任务对象
     */
    public TimeoutTaskDTO getTaskFromPrepareQueue(String bizType) {
        try {
            String key = PREPARE_QUEUE_PREFIX + bizType;
            String taskJson = redisService.popFromList(key);
            
            if (taskJson != null) {
                TimeoutTaskDTO task = JSON.parseObject(taskJson, TimeoutTaskDTO.class);
                log.info("从准备队列获取任务: bizType={}, bizId={}", task.getBizType(), task.getBizId());
                return task;
            }
            
            return null;
        } catch (Exception e) {
            log.error("从准备队列获取任务异常: bizType={}", bizType, e);
            return null;
        }
    }
    
    /**
     * 功能描述: 往死信队列添加任务
     *
     * @param timeoutTask 任务
     * @author zhanghaojie
     * @date 2024/01/15
     */
    @Override
    public void addTaskToDeadQueue(TimeoutTaskDTO timeoutTask) {
        try {
            String key = DEAD_QUEUE_PREFIX + timeoutTask.getBizType();
            String taskJson = JSON.toJSONString(timeoutTask);
            
            // 死信队列使用列表存储，保持FIFO顺序
            Long result = redisService.addToList(key, taskJson);
            
            if (result > 0) {
                log.warn("任务已添加到死信队列: bizType={}, bizId={}, retryCount={}", 
                    timeoutTask.getBizType(), timeoutTask.getBizId(), timeoutTask.getRetryCount());
            } else {
                log.error("任务添加到死信队列失败: bizType={}, bizId={}", 
                    timeoutTask.getBizType(), timeoutTask.getBizId());
            }
        } catch (Exception e) {
            log.error("添加任务到死信队列异常: bizType={}, bizId={}", 
                timeoutTask.getBizType(), timeoutTask.getBizId(), e);
        }
    }
    
    /**
     * 功能描述: 从死信队列删除任务
     *
     * @author zhanghaojie
     * @date 2024/01/15
     */
    @Override
    public void deleteTaskFromDeadQueue() {
        // 这个方法设计为从所有业务类型的死信队列中清理任务
        // 实际使用中可能需要传入具体的业务类型
        log.warn("deleteTaskFromDeadQueue方法需要传入具体的业务类型参数");
    }
    
    /**
     * 从指定业务类型的死信队列中获取任务
     *
     * @param bizType 业务类型
     * @return 任务对象
     */
    public TimeoutTaskDTO getTaskFromDeadQueue(String bizType) {
        try {
            String key = DEAD_QUEUE_PREFIX + bizType;
            String taskJson = redisService.popFromList(key);
            
            if (taskJson != null) {
                TimeoutTaskDTO task = JSON.parseObject(taskJson, TimeoutTaskDTO.class);
                log.info("从死信队列获取任务: bizType={}, bizId={}", task.getBizType(), task.getBizId());
                return task;
            }
            
            return null;
        } catch (Exception e) {
            log.error("从死信队列获取任务异常: bizType={}", bizType, e);
            return null;
        }
    }
    
    /**
     * 从存储队列中获取到期的任务
     *
     * @param bizType 业务类型
     * @param bizId   业务ID
     * @param currentTime 当前时间
     * @return 任务对象
     */
    public TimeoutTaskDTO getExpiredTaskFromStoreQueue(String bizType, String bizId, long currentTime) {
        try {
            String key = STORE_QUEUE_PREFIX + bizType + ":" + bizId;
            String taskJson = redisService.getFromSortedSet(key, currentTime);
            
            if (taskJson != null) {
                TimeoutTaskDTO task = JSON.parseObject(taskJson, TimeoutTaskDTO.class);
                log.info("从存储队列获取到期任务: bizType={}, bizId={}, actionTime={}", 
                    task.getBizType(), task.getBizId(), task.getActionTime());
                return task;
            }
            
            return null;
        } catch (Exception e) {
            log.error("从存储队列获取到期任务异常: bizType={}, bizId={}", bizType, bizId, e);
            return null;
        }
    }
    
    /**
     * 清理指定业务类型的队列
     *
     * @param bizType 业务类型
     */
    public void clearQueues(String bizType) {
        try {
            // 清理准备队列
            String prepareKey = PREPARE_QUEUE_PREFIX + bizType;
            redisService.getQueueAndDel(prepareKey);
            
            // 清理死信队列
            String deadKey = DEAD_QUEUE_PREFIX + bizType;
            redisService.getQueueAndDel(deadKey);
            
            log.info("已清理业务类型 {} 的队列", bizType);
        } catch (Exception e) {
            log.error("清理队列异常: bizType={}", bizType, e);
        }
    }
}
