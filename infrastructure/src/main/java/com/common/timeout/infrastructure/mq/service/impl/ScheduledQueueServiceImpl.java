package com.common.timeout.infrastructure.mq.service.impl;

import com.common.timeout.infrastructure.db.model.TimeoutTaskDTO;
import com.common.timeout.infrastructure.mq.service.QueueOperationService;
import com.common.timeout.infrastructure.task.ScheduledThreadPoolTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * QueueOperationServiceImpl
 * 功能描述：延迟线程池实现任务管理
 *
 * @author zhanghaojie
 * @date 2022/4/20 09:52
 */
@Service
@Slf4j
public class ScheduledQueueServiceImpl implements QueueOperationService {

    ScheduledThreadPoolExecutor executorServiceThreadFactory = new ScheduledThreadPoolExecutor(8);

    /**
     * 功能描述: 往待执行队列添加任务
     *
     * @param timeoutTask
     * @author zhanghaojie
     * @date 2022/3/17 11:40
     */
    @Override
    public void addTaskToStoreQueue(TimeoutTaskDTO timeoutTask) {
        Long delayTime = timeoutTask.getActionTime() - System.currentTimeMillis();
        if (delayTime < 0) {
            // 当前时间超过了执行时间 立即执行 （其实也是加入到线程池 不过延迟时间为0）
            executorServiceThreadFactory.execute(new ScheduledThreadPoolTask(timeoutTask.getBizId(), timeoutTask.getBizType()));
        }
        // 添加任务至延迟队列
        executorServiceThreadFactory.schedule(new ScheduledThreadPoolTask(timeoutTask.getBizId(), timeoutTask.getBizType()), delayTime, TimeUnit.MILLISECONDS);
    }

    /**
     * 功能描述: 往就绪队列添加任务
     *
     * @param timeoutTask@return
     * @author zhanghaojie
     * @date 2022/3/17 11:40
     */
    @Override
    public void addTaskToPrepareQueue(TimeoutTaskDTO timeoutTask) {

    }

    /**
     * 功能描述: 从就绪行队列删除
     *
     * @return
     * @author zhanghaojie
     * @date 2022/3/17 11:40
     */
    @Override
    public void deleteTaskFromPrepareQueue() {

    }

    /**
     * 功能描述: 往死信行队列添加任务
     *
     * @param timeoutTask 任务
     * @author zhanghaojie
     * @date 2022/3/17 11:40
     */
    @Override
    public void addTaskToDeadQueue(TimeoutTaskDTO timeoutTask) {

    }

    /**
     * 功能描述: 往死信行队列删除任务
     *
     * @return
     * @author zhanghaojie
     * @date 2022/3/17 11:40
     */
    @Override
    public void deleteTaskFromDeadQueue() {

    }
}
