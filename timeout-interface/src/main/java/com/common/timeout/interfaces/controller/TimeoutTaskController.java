package com.common.timeout.interfaces.controller;

import com.common.timeout.api.dto.AddTimeoutTaskDTO;
import com.common.timeout.api.dto.TimeoutTaskVO;
import com.common.timeout.api.dto.WebResponse;
import com.common.timeout.api.enums.TimeoutCenterStateEnum;
import com.common.timeout.infrastructure.TimeoutTaskService;
import com.common.timeout.infrastructure.db.model.TimeoutTaskDTO;
import com.common.timeout.infrastructure.mq.QueueOperationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * TimeoutTaskController
 * 功能描述: 任务管理API控制器
 *
 * @author zhanghaojie
 * @date 2024/01/15
 */
@Slf4j
@RestController
@RequestMapping("/api/timeout-task")
@Api(tags = "超时任务管理")
public class TimeoutTaskController {
    
    @Autowired
    private TimeoutTaskService timeoutTaskService;
    
    @Resource(name = "timeQueueServiceImpl")
    private QueueOperationService queueOperationService;
    
    @ApiOperation("查询超时任务")
    @GetMapping("/query")
    public WebResponse<TimeoutTaskVO> queryTimeoutTask(
            @ApiParam("业务类型") @RequestParam String bizType,
            @ApiParam("业务ID") @RequestParam String bizId) {
        
        try {
            TimeoutTaskDTO timeoutTask = new TimeoutTaskDTO();
            timeoutTask.setBizType(bizType);
            timeoutTask.setBizId(bizId);
            
            TimeoutTaskDTO queryValue = timeoutTaskService.queryTask(timeoutTask);
            if (Objects.isNull(queryValue)) {
                return WebResponse.returnSuccess();
            }
            
            TimeoutTaskVO timeoutTaskVO = new TimeoutTaskVO();
            BeanUtils.copyProperties(queryValue, timeoutTaskVO);
            return WebResponse.returnSuccess(timeoutTaskVO);
        } catch (Exception e) {
            log.error("查询超时任务失败: bizType={}, bizId={}", bizType, bizId, e);
            return WebResponse.returnFail("QUERY_ERROR", "查询失败: " + e.getMessage());
        }
    }
    
    @ApiOperation("添加超时任务")
    @PostMapping("/add")
    public WebResponse addTimeoutTask(@RequestBody AddTimeoutTaskDTO addTimeoutTaskDTO) {
        try {
            if (addTimeoutTaskDTO == null) {
                return WebResponse.returnFail("PARAM_ERROR", "任务参数不能为空");
            }
            
            TimeoutTaskDTO timeoutTask = new TimeoutTaskDTO();
            BeanUtils.copyProperties(addTimeoutTaskDTO, timeoutTask);
            timeoutTask.setState(TimeoutCenterStateEnum.WAIT.getCode());
            timeoutTask.setCreateTime(System.currentTimeMillis());
            timeoutTask.setUpdateTime(System.currentTimeMillis());
            timeoutTask.setRetryCount(0);
            
            // 保存到数据库
            Integer result = timeoutTaskService.addTask(timeoutTask);
            if (result < 1) {
                return WebResponse.returnFail("SAVE_ERROR", "任务保存失败");
            }
            
            // 添加到队列
            queueOperationService.addTaskToStoreQueue(timeoutTask);
            
            log.info("添加超时任务成功: bizType={}, bizId={}, actionTime={}", 
                addTimeoutTaskDTO.getBizType(), addTimeoutTaskDTO.getBizId(), addTimeoutTaskDTO.getActionTime());
            
            return WebResponse.returnSuccess();
        } catch (Exception e) {
            log.error("添加超时任务失败: bizType={}, bizId={}", 
                addTimeoutTaskDTO.getBizType(), addTimeoutTaskDTO.getBizId(), e);
            return WebResponse.returnFail("ADD_ERROR", "添加失败: " + e.getMessage());
        }
    }
    
    @ApiOperation("取消超时任务")
    @DeleteMapping("/cancel")
    public WebResponse cancelTimeoutTask(
            @ApiParam("业务类型") @RequestParam String bizType,
            @ApiParam("业务ID") @RequestParam String bizId) {
        
        try {
            // 尝试取消任务（只有待处理状态的任务才能被取消）
            Integer result = timeoutTaskService.updateTaskStateLock(
                TimeoutCenterStateEnum.CANCEL, TimeoutCenterStateEnum.WAIT, bizType, bizId);
            
            if (result > 0) {
                log.info("取消超时任务成功: bizType={}, bizId={}", bizType, bizId);
                return WebResponse.returnSuccess();
            }
            
            // 检查任务当前状态
            TimeoutTaskDTO timeoutTask = new TimeoutTaskDTO();
            timeoutTask.setBizType(bizType);
            timeoutTask.setBizId(bizId);
            TimeoutTaskDTO queryValue = timeoutTaskService.queryTask(timeoutTask);
            
            if (Objects.isNull(queryValue)) {
                return WebResponse.returnFail("TASK_NOT_FOUND", "任务不存在");
            }
            
            if (Objects.equals(queryValue.getState(), TimeoutCenterStateEnum.CANCEL.getCode())) {
                return WebResponse.returnSuccess();
            }
            
            if (Objects.equals(queryValue.getState(), TimeoutCenterStateEnum.SUCCESS.getCode()) ||
                Objects.equals(queryValue.getState(), TimeoutCenterStateEnum.EXECUTION.getCode())) {
                return WebResponse.returnFail("TASK_CANNOT_CANCEL", "任务已执行或正在执行中，无法取消");
            }
            
            return WebResponse.returnFail("CANCEL_FAILED", "取消任务失败");
        } catch (Exception e) {
            log.error("取消超时任务失败: bizType={}, bizId={}", bizType, bizId, e);
            return WebResponse.returnFail("CANCEL_ERROR", "取消失败: " + e.getMessage());
        }
    }
    
    @ApiOperation("重新执行任务")
    @PostMapping("/retry")
    public WebResponse retryTimeoutTask(
            @ApiParam("业务类型") @RequestParam String bizType,
            @ApiParam("业务ID") @RequestParam String bizId) {
        
        try {
            TimeoutTaskDTO timeoutTask = new TimeoutTaskDTO();
            timeoutTask.setBizType(bizType);
            timeoutTask.setBizId(bizId);
            
            TimeoutTaskDTO queryValue = timeoutTaskService.queryTask(timeoutTask);
            if (Objects.isNull(queryValue)) {
                return WebResponse.returnFail("TASK_NOT_FOUND", "任务不存在");
            }
            
            // 只有失败的任务才能重试
            if (!Objects.equals(queryValue.getState(), TimeoutCenterStateEnum.FAILED.getCode())) {
                return WebResponse.returnFail("TASK_STATE_ERROR", "只有失败状态的任务才能重试");
            }
            
            // 重置任务状态
            timeoutTaskService.updateTaskStateByBizTypeAndBizId(
                TimeoutCenterStateEnum.WAIT, bizType, bizId);
            
            // 重新加入队列
            queryValue.setState(TimeoutCenterStateEnum.WAIT.getCode());
            queryValue.setActionTime(System.currentTimeMillis()); // 立即执行
            queueOperationService.addTaskToStoreQueue(queryValue);
            
            log.info("重新执行任务成功: bizType={}, bizId={}", bizType, bizId);
            return WebResponse.returnSuccess();
        } catch (Exception e) {
            log.error("重新执行任务失败: bizType={}, bizId={}", bizType, bizId, e);
            return WebResponse.returnFail("RETRY_ERROR", "重试失败: " + e.getMessage());
        }
    }
    
    @ApiOperation("健康检查")
    @GetMapping("/health")
    public WebResponse<String> healthCheck() {
        return WebResponse.returnSuccess("OK");
    }
}
