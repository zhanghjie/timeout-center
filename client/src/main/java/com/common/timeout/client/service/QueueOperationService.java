package com.common.timeout.client.service;

import com.common.timeout.client.db.model.TimeoutTaskDTO;
import com.common.timeout.client.pojo.TaskRedisVO;

import java.util.List;

/**
 * QueueOperationService
 * 功能描述：redis队列操作
 *
 * @author zhanghaojie
 * @date 2022/3/17 11:27
 */
public interface QueueOperationService {

    /**
     * 功能描述: 往待执行队列添加任务
     *
     * @param
     * @return
     * @author zhanghaojie
     * @date 2022/3/17 11:40
     */
    void addTaskToStoreQueue(TimeoutTaskDTO timeoutTask);

    /**
     * 功能描述: 往就绪队列添加任务
     *
     * @param
     * @return
     * @author zhanghaojie
     * @date 2022/3/17 11:40
     */
    void addTaskToPrepareQueue(TimeoutTaskDTO timeoutTask);

    /**
     * 功能描述: 从就绪行队列删除
     *
     * @param
     * @author zhanghaojie
     * @date 2022/3/17 11:40
     */
    void deleteTaskFromPrepareQueue();


    /**
     * 功能描述: 往死信行队列添加任务
     *
     * @param
     * @return
     * @author zhanghaojie
     * @date 2022/3/17 11:40
     */
    void addTaskToDeadQueue(TimeoutTaskDTO timeoutTask);


    /**
     * 功能描述: 往死信行队列删除任务
     *
     * @param
     * @return
     * @author zhanghaojie
     * @date 2022/3/17 11:40
     */
    void deleteTaskFromDeadQueue();


}
