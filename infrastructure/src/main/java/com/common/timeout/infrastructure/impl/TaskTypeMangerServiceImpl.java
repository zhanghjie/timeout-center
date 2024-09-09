package com.common.timeout.infrastructure.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.common.timeout.infrastructure.TaskTypeMangerService;
import com.common.timeout.infrastructure.annotation.ToLog;
import com.common.timeout.infrastructure.cache.TimeoutCacheService;
import com.common.timeout.infrastructure.db.mapper.TaskTypeMangerMapper;
import com.common.timeout.infrastructure.db.model.TaskTypeMangerDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * TaskTypeMangerServiceImpl
 * 功能描述：任务类型管理服务实现类
 *
 * @author zhanghaojie
 * @date 2022/3/17 15:33
 */
@ToLog
@Service
public class TaskTypeMangerServiceImpl implements TaskTypeMangerService {
    @Autowired
    @SuppressWarnings("all")
    private TaskTypeMangerMapper taskTypeMangerMapper;

    @Autowired
    private TimeoutCacheService timeoutCacheService;

    private static final String ALL_TASK_TYPE = "allTaskType";

    private static final String ALL_TASK_TYPE_DTO = "allTaskTypeDTO";

    private static final String TASK_TYPE_DTO = "taskTypeDTO";

    /**
     * 功能描述: 获取所有的任务类型对象
     *
     * @return List<TaskTypeMangerDTO>
     * @author zhanghaojie
     * @date 2022/3/17 15:32
     */
    @Override
    public List<TaskTypeMangerDTO> getAllTaskTypeMangerDTO() {
        String value = timeoutCacheService.getValue(ALL_TASK_TYPE_DTO);
        if (StringUtils.isNotBlank(value)) {
            return JSONArray.parseArray(value, TaskTypeMangerDTO.class);
        }
        List<TaskTypeMangerDTO> taskTypeMangerDTOList = taskTypeMangerMapper.selectList(new QueryWrapper<>());
        if (CollectionUtils.isEmpty(taskTypeMangerDTOList)) {
            // 缓存空值 避免穿透
            timeoutCacheService.addValue(ALL_TASK_TYPE_DTO, JSON.toJSONString(taskTypeMangerDTOList));
            return new ArrayList<>();
        }
        timeoutCacheService.addValue(ALL_TASK_TYPE_DTO, JSON.toJSONString(taskTypeMangerDTOList));
        return taskTypeMangerDTOList;
    }


    /**
     * 功能描述: 获取所有的任务类型
     *
     * @return List<TaskTypeMangerDTO>
     * @author zhanghaojie
     * @date 2022/3/17 15:32
     */
    @Override
    public List<String> getAllTaskType() {
        String value = timeoutCacheService.getValue(ALL_TASK_TYPE);

        if (StringUtils.isNotBlank(value)) {
            return JSONArray.parseArray(value, String.class);
        }
        List<TaskTypeMangerDTO> mangerDTOList = getAllTaskTypeMangerDTO();
        if (CollectionUtils.isEmpty(mangerDTOList)) {
            // 缓存空值 避免穿透
            timeoutCacheService.addValue(ALL_TASK_TYPE, JSON.toJSONString(new ArrayList<>()));
            return new ArrayList<>();
        }
        List<String> taskTypeList = mangerDTOList.stream().map(TaskTypeMangerDTO::getBizType).collect(Collectors.toList());
        timeoutCacheService.addValue(ALL_TASK_TYPE, JSON.toJSONString(taskTypeList));
        return taskTypeList;
    }

    /**
     * 功能描述: 根据BizType获取任务类型
     *
     * @param bizType
     * @return List<TaskTypeMangerDTO>
     * @author zhanghaojie
     * @date 2022/3/17 15:32
     */
    @Override
    public TaskTypeMangerDTO getTaskTypeByBizType(String bizType) {
        String value = timeoutCacheService.getValue(TASK_TYPE_DTO + bizType);
        if (StringUtils.isNotBlank(value)) {
            return JSON.parseObject(value, TaskTypeMangerDTO.class);
        }
        TaskTypeMangerDTO queryMangerDTO = new TaskTypeMangerDTO(bizType);
        TaskTypeMangerDTO taskTypeMangerDTO = taskTypeMangerMapper.selectOne(new QueryWrapper<>(queryMangerDTO));
        if (Objects.isNull(taskTypeMangerDTO)) {
            // 缓存空值 避免穿透
            timeoutCacheService.addValue(TASK_TYPE_DTO + bizType, JSON.toJSONString(new TaskTypeMangerDTO()));
            return null;
        }
        timeoutCacheService.addValue(TASK_TYPE_DTO + bizType, JSON.toJSONString(taskTypeMangerDTO));
        return taskTypeMangerDTO;
    }
}
