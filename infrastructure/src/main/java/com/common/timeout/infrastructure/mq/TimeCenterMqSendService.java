package com.common.timeout.infrastructure.mq;

import com.common.timeout.infrastructure.db.model.TaskTypeMangerDTO;
import com.common.timeout.infrastructure.db.model.TimeoutTaskDTO;

/**
 * TimeCenterMqSendService
 * 功能描述：超时中心发送MQ 服务
 *
 * @author zhanghaojie
 * @date 2022/4/25 16:00
 */
public interface TimeCenterMqSendService {


    /**
     * 功能描述: 发送MQ 消息
     *
     * @param msg
     * @param taskTypeMangerDTO
     * @return
     * @author zhanghaojie
     * @date 2022/4/25 15:28
     */
    void sendMessage(TimeoutTaskDTO msg, TaskTypeMangerDTO taskTypeMangerDTO);
}
