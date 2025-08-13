package com.common.timeout.infrastructure.mq.impl;

import com.common.timeout.infrastructure.db.model.TimeoutTaskDTO;
import com.common.timeout.infrastructure.mq.QueueOperationService;
import com.common.timeout.infrastructure.timewheel.TimerWheelService;
import com.common.timeout.infrastructure.timewheel.vo.TimerTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * TimeQueueServiceImpl
 * 功能描述：时间轮算法实现
 *
 * @author zhanghaojie
 * @date 2022/8/17 17:37
 */
@Slf4j
@Service
public class TimeQueueServiceImpl implements QueueOperationService {
    @Autowired
    private TimerWheelService timerWheelService;

    /**
     * 功能描述: 往待执行队列添加任务
     *
     * @param timeoutTask 任务
     * @author zhanghaojie
     * @date 2022/3/17 11:40
     */
    @Override
    public void addTaskToStoreQueue(TimeoutTaskDTO timeoutTask) {
        TimerTask timerTask = new TimerTask(timeoutTask.getBizType(), timeoutTask.getBizId(),
                timeoutTask.getActionTime(), timeoutTask.getOrder());
        timerWheelService.add(timerTask);
    }

    /**
     * 功能描述: 往就绪队列添加任务
     *
     * @param timeoutTask 任务
     * @author zhanghaojie
     * @date 2022/3/17 11:40
     */
    @Override
    public void addTaskToPrepareQueue(TimeoutTaskDTO timeoutTask) {
        // 时间轮实现中，就绪队列的概念对应到时间轮的当前执行槽
        // 这里可以直接加入时间轮，或者标记为准备执行状态
        TimerTask timerTask = new TimerTask(timeoutTask.getBizType(), timeoutTask.getBizId(),
                timeoutTask.getActionTime(), timeoutTask.getOrder());
        timerWheelService.add(timerTask);
    }

    /**
     * 功能描述: 从就绪队列删除任务
     *
     * @author zhanghaojie
     * @date 2022/3/17 11:40
     */
    @Override
    public void deleteTaskFromPrepareQueue() {
        // 时间轮实现中，任务会自动从轮中移除
        // 这里主要用于清理或取消操作
        // 具体实现可能需要在TimerWheelService中添加删除方法
    }

    /**
     * 功能描述: 往死信队列添加任务
     *
     * @param timeoutTask 任务
     * @author zhanghaojie
     * @date 2022/3/17 11:40
     */
    @Override
    public void addTaskToDeadQueue(TimeoutTaskDTO timeoutTask) {
        // 对于失败的任务，可以考虑延迟重新加入时间轮
        // 或者使用专门的失败处理机制
        if (timeoutTask.getRetryCount() < 3) { // 最大重试3次
            // 延迟重试，延迟时间可以配置
            long retryDelay = 60000L; // 1分钟后重试
            timeoutTask.setActionTime(System.currentTimeMillis() + retryDelay);
            TimerTask retryTask = new TimerTask(timeoutTask.getBizType(), timeoutTask.getBizId(),
                    timeoutTask.getActionTime(), timeoutTask.getOrder());
            timerWheelService.add(retryTask);
        } else {
            // 超过重试次数，记录到死信队列（可以使用Redis或数据库）
            // 这里暂时只记录日志
            log.error("任务执行失败且超过最大重试次数: bizType={}, bizId={}, retryCount={}", 
                timeoutTask.getBizType(), timeoutTask.getBizId(), timeoutTask.getRetryCount());
        }
    }

    /**
     * 功能描述: 从死信队列删除任务
     *
     * @author zhanghaojie
     * @date 2022/3/17 11:40
     */
    @Override
    public void deleteTaskFromDeadQueue() {
        // 清理死信队列的实现
        // 可以定期清理或手动清理死信队列中的任务
        log.info("执行死信队列清理操作");
    }
}
