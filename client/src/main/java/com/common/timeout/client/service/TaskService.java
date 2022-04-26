package com.common.timeout.client.service;


/**
 * TaskService
 * 功能描述：任务操作服务
 *
 * @author zhanghaojie
 * @date 2022/4/24 17:32
 */
public interface TaskService {

    /**
     * 功能描述: 执行任务
     *
     * @param bizType
     * @param bizId
     * @author zhanghaojie
     * @date 2022/4/24 18:40
     */
    void doTask(String bizType, String bizId);

    /**
     * 功能描述: 发送Mq 消息失败回调
     *
     * @param bizType
     * @param bizId
     * @author zhanghaojie
     * @date 2022/4/25 15:52
     */
    void doSendMQFailed(String bizType, String bizId);

    /**
     * 功能描述: 发送Mq 消息成功回调
     *
     * @param bizType
     * @param bizId
     * @author zhanghaojie
     * @date 2022/4/25 15:52
     */
    void doSendMQSuccess(String bizType, String bizId);
}
