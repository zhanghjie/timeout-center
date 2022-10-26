package com.common.timeout.infrastructure.mq.service.impl;

import com.common.timeout.infrastructure.db.model.TimeoutTaskDTO;
import com.common.timeout.infrastructure.mq.service.QueueOperationService;
import com.common.timeout.infrastructure.timewheel.TimerWheelService;
import com.common.timeout.infrastructure.timewheel.vo.TimerTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * TimeQueueServiceImpl
 * 功能描述：时间轮算法实现
 *
 * @author zhanghaojie
 * @date 2022/8/17 17:37
 */
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
        TimerTask timerTask = new TimerTask(timeoutTask.getBizType(), timeoutTask.getBizId(), timeoutTask.getActionTime());
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

    }

    /**
     * 功能描述: 从就绪行队列删除
     *
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
