package com.common.timeout.client.db.service;

import com.common.timeout.client.db.model.TimeoutTaskDTO;

/**
 * TimeoutTaskService
 * 功能描述: 超时中心表服务聚合
 *
 * @author zhanghaojie
 * @date 2021/12/18 15:24
 */
public interface TimeoutTaskService {

    /**
     * 修改超时中心任务状态
     *
     * @param bizType
     * @param bizId
     * @return Integer
     * @author zhanghaojie
     * @date 2021/12/18 15:29
     */
    Integer updateTaskStatusByBizTypeAndBizId(String bizType, String bizId);

    /**
     * 查询超时中心
     *
     * @param taskWrapper
     * @return TimeoutTask
     * @author zhanghaojie
     * @date 2021/12/18 15:29
     */
    TimeoutTaskDTO queryTask(TimeoutTaskDTO taskWrapper);

    /**
     * 添加超时中心任务
     *
     * @param addTimeoutTaskDTO
     * @return Integer
     * @author zhanghaojie
     * @date 2021/12/18 15:29
     */
    Integer addTask(TimeoutTaskDTO addTimeoutTaskDTO);

}
