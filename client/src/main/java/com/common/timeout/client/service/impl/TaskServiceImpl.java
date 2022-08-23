package com.common.timeout.client.service.impl;

import com.common.timeout.api.enums.TimeoutCenterStateEnum;
import com.common.timeout.client.db.model.TaskTypeMangerDTO;
import com.common.timeout.client.db.model.TimeoutTaskDTO;
import com.common.timeout.client.db.service.TaskTypeMangerService;
import com.common.timeout.client.db.service.TimeoutTaskService;
import com.common.timeout.client.mq.TimeCenterMqSendService;
import com.common.timeout.client.service.QueueOperationService;
import com.common.timeout.client.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

/**
 * TaskServiceImpl
 * 功能描述：TODO
 *
 * @author zhanghaojie
 * @date 2022/4/24 21:39
 */
@Service
@Slf4j
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TimeoutTaskService timeoutTaskService;

    @Autowired
    private TaskTypeMangerService taskTypeMangerService;

    @Autowired
    private QueueOperationService queueOperationService;

    @Autowired
    private TimeCenterMqSendService timeCenterMqSendService;


    // 失败后重试间隔时间（ms）,默认为 0 后面改成配置化
    private final static Integer RETRY_TIME = 0;

    // 发送Mq失败后重试次数 默认重试3次 后面改成配置化
    private final static Integer RETRY_COUNT = 3;

    /**
     * 功能描述: 执行任务
     *
     * @param bizType
     * @param bizId
     * @return
     * @author zhanghaojie
     * @date 2022/4/24 18:40
     */
    @Override
    public void doTask(String bizType, String bizId) {
        TaskTypeMangerDTO taskTypeMangerDTO = taskTypeMangerService.getTaskTypeByBizType(bizType);
        if (Objects.isNull(taskTypeMangerDTO) || RETRY_COUNT <= 0) {
            return;
        }
        Integer updateResult = timeoutTaskService.updateTaskStateLock(TimeoutCenterStateEnum.EXECUTION,
                TimeoutCenterStateEnum.WAIT, bizType, bizId);
        if (Objects.isNull(updateResult) || updateResult < 1) {
            log.info("任务状态发生改变,bizType：{},bizId：{}", bizType, bizId);
            return;
        }
        TimeoutTaskDTO taskWrapper = new TimeoutTaskDTO();
        taskWrapper.setBizType(bizType);
        taskWrapper.setBizId(bizId);
        // 查询出待发送任务的具体内容
        TimeoutTaskDTO timeoutTaskDTO = timeoutTaskService.queryTask(taskWrapper);
        if (Objects.isNull(timeoutTaskDTO) || !Objects.equals(timeoutTaskDTO.getState(), TimeoutCenterStateEnum.EXECUTION.getCode())) {
            return;
        }
        // 发送是否成功 等待回调处理
        timeCenterMqSendService.sendMessage(timeoutTaskDTO, taskTypeMangerDTO);
    }

    /**
     * 功能描述: 发送Mq 消息失败回调
     *
     * @param bizType
     * @param bizId
     * @author zhanghaojie
     * @date 2022/4/25 15:52
     */
    @Override
    public void doSendMessageFailed(String bizType, String bizId) {
        log.error("发送MQ消息失败，bizType:{},bizId:{}", bizType, bizId);
        TaskTypeMangerDTO taskTypeMangerDTO = taskTypeMangerService.getTaskTypeByBizType(bizType);
        if (Objects.isNull(taskTypeMangerDTO) || RETRY_COUNT <= 0) {
            return;
        }
        TimeoutTaskDTO timeoutTask = new TimeoutTaskDTO();
        timeoutTask.setBizType(bizType);
        timeoutTask.setBizId(bizId);
        TimeoutTaskDTO timeoutTaskDTO = timeoutTaskService.queryTask(timeoutTask);
        if (Objects.isNull(timeoutTaskDTO) || !Objects.equals(timeoutTaskDTO.getState(), TimeoutCenterStateEnum.EXECUTION.getCode())) {
            return;
        }
        Integer retryCount = timeoutTaskDTO.getRetryCount() + 1;
        // 达到了重试上限次数
        if (retryCount > RETRY_COUNT) {
            // 修改状态为失败 不管修改成功与否都停止
            timeoutTaskService.updateTaskStateByBizTypeAndBizId(TimeoutCenterStateEnum.FAILED,
                    timeoutTaskDTO.getBizType(), timeoutTaskDTO.getBizId());
            return;
        }
        // 需要进行重试 修改状态为待处理 等待下次执行
        Integer result = timeoutTaskService.updateTaskStateLock(TimeoutCenterStateEnum.WAIT, TimeoutCenterStateEnum.EXECUTION,
                timeoutTaskDTO.getBizType(), timeoutTaskDTO.getBizId());
        // 修改为初始状态失败的话 也不做处理，认为有其他线程干预
        if (result < 0) {
            return;
        }
        timeoutTaskDTO.setActionTime(System.currentTimeMillis() + RETRY_TIME);
        // 向任务队列添加任务
        queueOperationService.addTaskToDeadQueue(timeoutTaskDTO);
        // 修改重试次数
        timeoutTaskService.addTaskRetryCount(timeoutTaskDTO.getBizType(), timeoutTaskDTO.getBizId());
    }

    /**
     * 功能描述: 发送Mq 消息成功回调
     *
     * @param bizType
     * @param bizId
     * @author zhanghaojie
     * @date 2022/4/25 15:52
     */
    @Override
    public void doSendMessageSuccess(String bizType, String bizId) {
        log.info("成功发送MQ消息，bizType:{},bizId:{}", bizType, bizId);
        timeoutTaskService.updateTaskStateByBizTypeAndBizId(TimeoutCenterStateEnum.SUCCESS, bizType, bizId);
    }


}
