package com.common.timeout.client.impl;


import com.alibaba.fastjson.JSON;
import com.common.timeout.client.db.model.TimeoutTaskDTO;
import com.common.timeout.client.db.service.TaskTypeMangerService;
import com.common.timeout.client.db.service.TimeoutTaskService;
import com.google.common.base.Preconditions;
import com.common.timeout.api.TimeoutCenterService;
import com.common.timeout.api.dto.AddTimeoutTaskDTO;
import com.common.timeout.api.dto.TimeoutTaskVO;
import com.common.timeout.api.dto.WebResponse;
import com.common.timeout.api.enums.TimeoutCenterStateEnum;
import com.common.timeout.client.service.QueueOperationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

/**
 * TimeoutCenterServiceImpl
 * 功能描述: 超时中心服务
 *
 * @author zhanghaojie
 * @date 2021/12/14 14:36
 */
@Slf4j
public class TimeoutCenterServiceImpl implements TimeoutCenterService {

    @Autowired
    private TimeoutTaskService timeoutTaskService;

    @Autowired
    private TaskTypeMangerService typeMangerService;

    @Autowired
    private QueueOperationService queueOperationService;

    /**
     * 查询超时中心任务
     *
     * @param bizType 业务类型
     * @param bizId   业务id
     * @return TimeoutTaskDTO
     * @author zhanghaojie
     * @date 2021/12/13 18:49
     */
    @Override
    public WebResponse<TimeoutTaskVO> queryTimeoutTask(String bizType, String bizId) {
        TimeoutTaskDTO timeoutTask = new TimeoutTaskDTO();
        timeoutTask.setBizType(bizType);
        timeoutTask.setBizId(bizId);
        TimeoutTaskDTO queryValue = timeoutTaskService.queryTask(timeoutTask);
        if (Objects.isNull(queryValue)) {
            // 查询为空也认为是正常的操作
            return WebResponse.returnSuccess();
        }
        TimeoutTaskVO timeoutTaskVO = new TimeoutTaskVO();
        BeanUtils.copyProperties(queryValue, timeoutTaskVO);
        return WebResponse.returnSuccess(timeoutTaskVO);
    }

    /**
     * 添加超时中心任务
     *
     * @param addTimeoutTaskDTO 添加超时中心任务对象
     * @return TimeoutTaskDTO
     * @author zhanghaojie
     * @date 2021/12/13 18:49
     */
    @Override
    public WebResponse addTimeoutTask(AddTimeoutTaskDTO addTimeoutTaskDTO) {
        if (typeMangerService.getAllTaskType().contains(addTimeoutTaskDTO.getBizType())) {
            log.error("添加超时中心任务 bizType不存在：{}", JSON.toJSONString(addTimeoutTaskDTO));
            return WebResponse.returnFail("10001", "bizType不存在");
        }
        Preconditions.checkNotNull(addTimeoutTaskDTO);
        Preconditions.checkNotNull(addTimeoutTaskDTO.getBizId());
        Preconditions.checkNotNull(addTimeoutTaskDTO.getBizType());
        Preconditions.checkNotNull(addTimeoutTaskDTO.getActionTime());
        TimeoutTaskDTO timeoutTask = new TimeoutTaskDTO();
        BeanUtils.copyProperties(timeoutTask, addTimeoutTaskDTO);
        //保存
        Integer result = timeoutTaskService.addTask(timeoutTask);
        if (result < 1) {
            return WebResponse.returnFail("10002", "任务保存不成功");
        }
        //往延迟队列中添加对象
        queueOperationService.addTaskToStoreQueue(timeoutTask);
        return WebResponse.returnSuccess();
    }

    /**
     * 取消超时中心任务
     *
     * @param bizType 业务类型
     * @param bizId   业务id
     * @author zhanghaojie
     * @date 2021/12/13 18:49
     */
    @Override
    public WebResponse cancelTimeoutTask(String bizType, String bizId) {

        if (typeMangerService.getAllTaskType().contains(bizType)) {
            log.error("取消超时中心任务 bizType不存在：{},bizId:{}", bizId, bizId);
            return WebResponse.returnFail("10001", "bizType不存在");
        }
        TimeoutTaskDTO timeoutTask = new TimeoutTaskDTO();
        timeoutTask.setBizType(bizType);
        timeoutTask.setBizId(bizId);
        TimeoutTaskDTO queryValue = timeoutTaskService.queryTask(timeoutTask);
        if (Objects.isNull(queryValue) || Objects.equals(queryValue.getState(), TimeoutCenterStateEnum.CANCEL.getCode())) {
            return WebResponse.returnSuccess();
        }
        Integer result = timeoutTaskService.updateTaskStatusByBizTypeAndBizId(bizType, bizId);
        if (result < 1) {
            return WebResponse.returnFail("20001", "任务保存不成功");
        }
        //删除待执行队列中的值
        queueOperationService.deleteTaskFromStoreQueue(bizType, bizId, queryValue.getActionTime());
        return WebResponse.returnSuccess();
    }
}
