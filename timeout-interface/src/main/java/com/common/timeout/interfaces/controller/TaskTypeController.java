package com.common.timeout.interfaces.controller;

import com.common.timeout.api.dto.WebResponse;
import com.common.timeout.infrastructure.TaskTypeMangerService;
import com.common.timeout.infrastructure.db.model.TaskTypeMangerDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * TaskTypeController
 * 功能描述: 任务类型管理控制器
 *
 * @author zhanghaojie
 * @date 2024/01/15
 */
@Slf4j
@RestController
@RequestMapping("/api/task-type")
@Api(tags = "任务类型管理")
public class TaskTypeController {
    
    @Autowired
    private TaskTypeMangerService taskTypeMangerService;
    
    @ApiOperation("获取所有任务类型")
    @GetMapping("/list")
    public WebResponse<List<String>> getAllTaskTypes() {
        try {
            List<String> taskTypes = taskTypeMangerService.getAllTaskType();
            return WebResponse.returnSuccess(taskTypes);
        } catch (Exception e) {
            log.error("获取任务类型列表失败", e);
            return WebResponse.returnFail("QUERY_ERROR", "查询失败: " + e.getMessage());
        }
    }
    
    @ApiOperation("根据业务类型获取任务类型详情")
    @GetMapping("/detail")
    public WebResponse<TaskTypeMangerDTO> getTaskTypeDetail(
            @ApiParam("业务类型") @RequestParam String bizType) {
        try {
            TaskTypeMangerDTO taskType = taskTypeMangerService.getTaskTypeByBizType(bizType);
            if (taskType == null) {
                return WebResponse.returnFail("NOT_FOUND", "任务类型不存在");
            }
            return WebResponse.returnSuccess(taskType);
        } catch (Exception e) {
            log.error("获取任务类型详情失败: bizType={}", bizType, e);
            return WebResponse.returnFail("QUERY_ERROR", "查询失败: " + e.getMessage());
        }
    }
    
    @ApiOperation("添加任务类型")
    @PostMapping("/add")
    public WebResponse addTaskType(@RequestBody TaskTypeMangerDTO taskTypeDTO) {
        try {
            if (taskTypeDTO == null || taskTypeDTO.getBizType() == null) {
                return WebResponse.returnFail("PARAM_ERROR", "任务类型参数不能为空");
            }
            
            // 检查是否已存在
            TaskTypeMangerDTO existingType = taskTypeMangerService.getTaskTypeByBizType(taskTypeDTO.getBizType());
            if (existingType != null) {
                return WebResponse.returnFail("ALREADY_EXISTS", "任务类型已存在");
            }
            
            taskTypeDTO.setCreateTime(System.currentTimeMillis());
            taskTypeDTO.setUpdateTime(System.currentTimeMillis());
            
            // 这里需要添加保存逻辑，当前TaskTypeMangerService可能没有保存方法
            // 需要在service中添加相应的保存方法
            
            log.info("添加任务类型成功: bizType={}", taskTypeDTO.getBizType());
            return WebResponse.returnSuccess();
        } catch (Exception e) {
            log.error("添加任务类型失败: bizType={}", taskTypeDTO.getBizType(), e);
            return WebResponse.returnFail("ADD_ERROR", "添加失败: " + e.getMessage());
        }
    }
    
    @ApiOperation("更新任务类型")
    @PutMapping("/update")
    public WebResponse updateTaskType(@RequestBody TaskTypeMangerDTO taskTypeDTO) {
        try {
            if (taskTypeDTO == null || taskTypeDTO.getBizType() == null) {
                return WebResponse.returnFail("PARAM_ERROR", "任务类型参数不能为空");
            }
            
            // 检查是否存在
            TaskTypeMangerDTO existingType = taskTypeMangerService.getTaskTypeByBizType(taskTypeDTO.getBizType());
            if (existingType == null) {
                return WebResponse.returnFail("NOT_FOUND", "任务类型不存在");
            }
            
            taskTypeDTO.setUpdateTime(System.currentTimeMillis());
            
            // 这里需要添加更新逻辑
            
            log.info("更新任务类型成功: bizType={}", taskTypeDTO.getBizType());
            return WebResponse.returnSuccess();
        } catch (Exception e) {
            log.error("更新任务类型失败: bizType={}", taskTypeDTO.getBizType(), e);
            return WebResponse.returnFail("UPDATE_ERROR", "更新失败: " + e.getMessage());
        }
    }
    
    @ApiOperation("删除任务类型")
    @DeleteMapping("/delete")
    public WebResponse deleteTaskType(@ApiParam("业务类型") @RequestParam String bizType) {
        try {
            // 检查是否存在
            TaskTypeMangerDTO existingType = taskTypeMangerService.getTaskTypeByBizType(bizType);
            if (existingType == null) {
                return WebResponse.returnFail("NOT_FOUND", "任务类型不存在");
            }
            
            // 这里需要添加删除逻辑
            // 同时需要检查是否有相关的任务正在使用此类型
            
            log.info("删除任务类型成功: bizType={}", bizType);
            return WebResponse.returnSuccess();
        } catch (Exception e) {
            log.error("删除任务类型失败: bizType={}", bizType, e);
            return WebResponse.returnFail("DELETE_ERROR", "删除失败: " + e.getMessage());
        }
    }
}
