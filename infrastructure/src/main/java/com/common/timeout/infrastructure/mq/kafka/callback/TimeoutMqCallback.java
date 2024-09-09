package com.common.timeout.infrastructure.mq.kafka.callback;


import com.common.timeout.infrastructure.task.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.concurrent.ListenableFutureCallback;

/**
 * MqCallback
 * 功能描述：消息发送回调方法
 *
 * @author zhanghaojie
 * @date 2022/4/25 15:44
 */
public class TimeoutMqCallback implements ListenableFutureCallback {

    private final String bizType;

    private final String bizId;


    @Autowired
    private TaskService taskService;

    public TimeoutMqCallback(String bizType, String bizId) {
        this.bizType = bizType;
        this.bizId = bizId;
    }

    /**
     * 功能描述: 发送Mq 消息失败回调
     *
     * @author zhanghaojie
     * @date 2022/4/25 15:52
     */
    @Override
    public void onFailure(Throwable ex) {
        taskService.doSendMessageFailed(this.bizType, this.bizId);
    }


    /**
     * 功能描述: 发送Mq消息成功回调
     *
     * @author zhanghaojie
     * @date 2022/4/25 15:52
     */
    @Override
    public void onSuccess(Object result) {
        // 这里不考虑消息重复发送的问题
        // 当接收到消息时 直接把状态改为成功即可
        taskService.doSendMessageSuccess(bizType, bizId);
    }

}
