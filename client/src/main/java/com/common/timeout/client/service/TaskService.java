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
     * @param bizType
     * @return
     * @author zhanghaojie
     * @date 2022/4/24 18:40
     */
    void doTask(String bizType, String bizId);
}
