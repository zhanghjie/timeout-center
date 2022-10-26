package com.common.timeout.infrastructure.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.common.timeout.infrastructure.TaskTypeMangerService;
import com.common.timeout.infrastructure.cache.ehcache.DefaultEhcacheUtils;
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
@Service
public class TaskTypeMangerServiceImpl implements TaskTypeMangerService {
    @Autowired
    @SuppressWarnings("all")
    private TaskTypeMangerMapper taskTypeMangerMapper;

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
        List<TaskTypeMangerDTO> taskTypeMangerDTOList = (List<TaskTypeMangerDTO>) DefaultEhcacheUtils.get(ALL_TASK_TYPE_DTO);
        if (!Objects.isNull(taskTypeMangerDTOList)) {
            return taskTypeMangerDTOList;
        }
        taskTypeMangerDTOList = taskTypeMangerMapper.selectList(new QueryWrapper<>());
        if (CollectionUtils.isEmpty(taskTypeMangerDTOList)) {
            // 缓存空值 避免穿透
            DefaultEhcacheUtils.put(ALL_TASK_TYPE_DTO, new ArrayList<>());
            return new ArrayList<>();
        }
        DefaultEhcacheUtils.put(ALL_TASK_TYPE_DTO, taskTypeMangerDTOList);
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
        List<String> taskTypeList = (List<String>) DefaultEhcacheUtils.get(ALL_TASK_TYPE);
        if (!Objects.isNull(taskTypeList)) {
            return taskTypeList;
        }
        List<TaskTypeMangerDTO> mangerDTOList = getAllTaskTypeMangerDTO();
        if (CollectionUtils.isEmpty(mangerDTOList)) {
            // 缓存空值 避免穿透
            DefaultEhcacheUtils.put(ALL_TASK_TYPE, new ArrayList<>());
            return new ArrayList<>();
        }
        taskTypeList = mangerDTOList.stream().map(TaskTypeMangerDTO::getBizType).collect(Collectors.toList());
        DefaultEhcacheUtils.put(ALL_TASK_TYPE, taskTypeList);
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
        TaskTypeMangerDTO taskTypeMangerDTO = (TaskTypeMangerDTO) DefaultEhcacheUtils.get(TASK_TYPE_DTO + bizType);
        if (!Objects.isNull(taskTypeMangerDTO) && StringUtils.isNotBlank(taskTypeMangerDTO.getBizType())) {
            return taskTypeMangerDTO;
        }
        TaskTypeMangerDTO queryMangerDTO = new TaskTypeMangerDTO(bizType);
        taskTypeMangerDTO = taskTypeMangerMapper.selectOne(new QueryWrapper<>(queryMangerDTO));
        if (!Objects.isNull(taskTypeMangerDTO)) {
            taskTypeMangerDTO = new TaskTypeMangerDTO();
            taskTypeMangerDTO.setBizType(bizType);
            // 缓存空值 避免穿透
            DefaultEhcacheUtils.put(TASK_TYPE_DTO + bizType, new TaskTypeMangerDTO());
            return null;
        }
        DefaultEhcacheUtils.put(TASK_TYPE_DTO + bizType, taskTypeMangerDTO);
        return taskTypeMangerDTO;
    }
}
