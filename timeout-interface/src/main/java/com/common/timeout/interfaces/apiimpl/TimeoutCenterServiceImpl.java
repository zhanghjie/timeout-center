package com.common.timeout.interfaces.apiimpl;


import com.alibaba.fastjson.JSON;
import com.common.timeout.api.TimeoutCenterService;
import com.common.timeout.api.dto.AddTimeoutTaskDTO;
import com.common.timeout.api.dto.TimeoutTaskVO;
import com.common.timeout.api.dto.WebResponse;
import com.common.timeout.api.enums.TimeoutCenterStateEnum;
import com.common.timeout.infrastructure.TaskTypeMangerService;
import com.common.timeout.infrastructure.TimeoutTaskService;
import com.common.timeout.infrastructure.db.model.TimeoutTaskDTO;
import com.common.timeout.infrastructure.enums.TimeoutCenterCodeEnum;
import com.common.timeout.infrastructure.mq.QueueOperationService;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.rmi.Naming;
import java.util.Objects;

/**
 * TimeoutCenterServiceImpl
 * 功能描述: 超时中心服务
 *
 * @author zhanghaojie
 * @date 2021/12/14 14:36
 */
@Slf4j
@Service
public class TimeoutCenterServiceImpl implements TimeoutCenterService , Serializable {
    private static final long serialVersionUID = -1;

    @Autowired
    private TimeoutTaskService timeoutTaskService;

    @Resource(name = "taskTypeMangerServiceImpl")
    private TaskTypeMangerService typeMangerService;

    @Resource(name = "timeQueueServiceImpl")
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
//        TimeoutTaskDTO queryValue = new TimeoutTaskDTO();
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
        // 只有待处理的任务才能被取消
        // 避免任务已经变更状态 而被改为已取消
        Integer result = timeoutTaskService.updateTaskStateLock(TimeoutCenterStateEnum.CANCEL, TimeoutCenterStateEnum.WAIT, bizType, bizId);
        if (result > 0) {
            // 正常取消完成
            return WebResponse.returnSuccess();
        }
        TimeoutTaskDTO timeoutTask = new TimeoutTaskDTO();
        timeoutTask.setBizType(bizType);
        timeoutTask.setBizId(bizId);
        TimeoutTaskDTO queryValue = timeoutTaskService.queryTask(timeoutTask);

        if (Objects.isNull(queryValue)) {
            // 查询定时任务为空
            return WebResponse.returnFail(TimeoutCenterCodeEnum.TASK_IS_EMPTY.getCode(),
                    TimeoutCenterCodeEnum.TASK_IS_EMPTY.getMessage());
        }
        if (Objects.equals(queryValue.getState(), TimeoutCenterStateEnum.CANCEL.getCode())) {
            // 或许是其他线程完成的取消 最终状态一致，视为成功
            return WebResponse.returnSuccess();
        }
        if (Objects.equals(queryValue.getState(), TimeoutCenterStateEnum.SUCCESS.getCode()) ||
                Objects.equals(queryValue.getState(), TimeoutCenterStateEnum.EXECUTION.getCode())) {
            // 任务已执行或正在执行中
            return WebResponse.returnFail(TimeoutCenterCodeEnum.TASK_IS_SUCCESS.getCode(),
                    TimeoutCenterCodeEnum.TASK_IS_SUCCESS.getMessage());
        }
        // 其他情况视为失败
        return WebResponse.returnFail(TimeoutCenterCodeEnum.CANCEL_TASK_FAIL.getCode(),
                TimeoutCenterCodeEnum.CANCEL_TASK_FAIL.getMessage());
    }
}
