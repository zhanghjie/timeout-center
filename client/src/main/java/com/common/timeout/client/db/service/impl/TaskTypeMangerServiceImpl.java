package com.common.timeout.client.db.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.common.timeout.client.db.service.TaskTypeMangerService;
import com.common.timeout.client.cache.ehcache.DefaultEhcacheUtils;
import com.common.timeout.client.db.mapper.TaskTypeMangerMapper;
import com.common.timeout.client.db.model.TaskTypeMangerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
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
    private TaskTypeMangerMapper taskTypeMangerMapper;

    private static final String ALL_TASK_TYPE = "allTaskType";

    private static final String ALL_TASK_TYPE_DTO = "allTaskTypeDTO";

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
        if (!CollectionUtils.isEmpty(taskTypeMangerDTOList)) {
            return taskTypeMangerDTOList;
        }
        taskTypeMangerDTOList = taskTypeMangerMapper.selectList(new QueryWrapper<>());
        if (CollectionUtils.isEmpty(taskTypeMangerDTOList)) {
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
        if (!CollectionUtils.isEmpty(taskTypeList)) {
            return taskTypeList;
        }
        List<TaskTypeMangerDTO> mangerDTOList = getAllTaskTypeMangerDTO();
        if (CollectionUtils.isEmpty(mangerDTOList)) {
            return new ArrayList<>();
        }
        taskTypeList = mangerDTOList.stream().map(TaskTypeMangerDTO::getBizType).collect(Collectors.toList());
        DefaultEhcacheUtils.put(ALL_TASK_TYPE, taskTypeList);
        return taskTypeList;
    }
}
