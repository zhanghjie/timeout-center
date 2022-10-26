package com.common.timeout.infrastructure.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.common.timeout.api.enums.TimeoutCenterStateEnum;
import com.common.timeout.infrastructure.TimeoutTaskService;
import com.common.timeout.infrastructure.db.mapper.TimeoutTaskMapper;
import com.common.timeout.infrastructure.db.model.TimeoutTaskDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * TimeoutTaskServiceImpl
 * 功能描述: 服务实现类
 *
 * @author zhanghaojie
 * @date 2021/12/18 15:24
 */
@Service
public class TimeoutTaskServiceImpl implements TimeoutTaskService {

    @Autowired
    private TimeoutTaskMapper timeoutTaskMapper;

    /**
     * 修改超时中心任务状态
     * <p>
     * 利用 state 做一个乐观锁
     *
     * @param bizType
     * @param bizId
     * @return Integer
     * @author zhanghaojie
     * @date 2021/12/18 15:29
     */
    @Override
    public Integer updateTaskStateLock(TimeoutCenterStateEnum stateEnum, TimeoutCenterStateEnum oldStateEnum, String bizType, String bizId) {
        if (StringUtils.isBlank(bizType) || StringUtils.isBlank(bizId)) {
            return 0;
        }
        return timeoutTaskMapper.updateStateByBizTypeAndBizIdAndOldState(stateEnum.getCode(), bizType, bizId, System.currentTimeMillis(), oldStateEnum.getCode());
    }

    /**
     * 超时重试次数+1
     * <p>
     * 利用 state 做一个乐观锁
     *
     * @param bizType
     * @param bizId
     * @return Integer
     * @author zhanghaojie
     * @date 2021/12/18 15:29
     */
    @Override
    public Integer addTaskRetryCount(String bizType, String bizId) {
        if (StringUtils.isBlank(bizType) || StringUtils.isBlank(bizId)) {
            return 0;
        }
        return timeoutTaskMapper.addTaskRetryCount(bizType, bizId, System.currentTimeMillis());
    }

    /**
     * 修改超时中心任务状态
     *
     * @param stateEnum
     * @param bizType
     * @param bizId
     * @return Integer
     * @author zhanghaojie
     * @date 2021/12/18 15:29
     */
    @Override
    public Integer updateTaskStateByBizTypeAndBizId(TimeoutCenterStateEnum stateEnum, String bizType, String bizId) {
        if (StringUtils.isBlank(bizType) || StringUtils.isBlank(bizId)) {
            return 0;
        }
        return timeoutTaskMapper.updateStateByBizTypeAndBizId(stateEnum.getCode(), bizType, bizId, System.currentTimeMillis());
    }

    /**
     * 查询超时中心
     *
     * @param timeoutTask
     * @return TimeoutTask
     * @author zhanghaojie
     * @date 2021/12/18 15:29
     */
    @Override
    public TimeoutTaskDTO queryTask(TimeoutTaskDTO timeoutTask) {
        if (Objects.isNull(timeoutTask.getId()) &&
                (StringUtils.isBlank(timeoutTask.getBizType()) || StringUtils.isBlank(timeoutTask.getBizId()))) {
            return null;
        }
        Wrapper<TimeoutTaskDTO> taskWrapper = new QueryWrapper<>(timeoutTask);
        TimeoutTaskDTO queryValue = timeoutTaskMapper.selectOne(taskWrapper);
        if (Objects.isNull(queryValue)) {
            return null;
        }
        return queryValue;
    }

    /**
     * 添加超时中心任务
     *
     * @param addTimeoutTaskDTO
     * @return TimeoutTask
     * @author zhanghaojie
     * @date 2021/12/18 15:29
     */
    @Override
    public Integer addTask(TimeoutTaskDTO addTimeoutTaskDTO) {
        return timeoutTaskMapper.insert(addTimeoutTaskDTO);
    }
}
