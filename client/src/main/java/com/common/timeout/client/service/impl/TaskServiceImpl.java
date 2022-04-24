package com.common.timeout.client.service.impl;

import com.common.timeout.api.enums.TimeoutCenterStateEnum;
import com.common.timeout.client.db.model.TimeoutTaskDTO;
import com.common.timeout.client.db.service.TimeoutTaskService;
import com.common.timeout.client.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * TaskServiceImpl
 * 功能描述：TODO
 *
 * @author zhanghaojie
 * @date 2022/4/24 21:39
 */
@Service
@Slf4j
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TimeoutTaskService timeoutTaskService;

    /**
     * 功能描述: 执行任务
     *
     * @param bizType
     * @param bizId
     * @return
     * @author zhanghaojie
     * @date 2022/4/24 18:40
     */
    @Override
    public void doTask(String bizType, String bizId) {
        System.out.println(1);
        TimeoutTaskDTO taskWrapper = new TimeoutTaskDTO();
        taskWrapper.setBizType(bizType);
        taskWrapper.setBizId(bizId);

        Integer updateResult = timeoutTaskService.updateTaskStateLock(TimeoutCenterStateEnum.EXECUTION,
                TimeoutCenterStateEnum.WAIT, bizType, bizId);
        if (Objects.isNull(updateResult) || updateResult < 1) {
            log.info("任务状态发生改变,bizType：{},bizId：{}", bizType, bizId);
            return;
        }
        // todo 发送 MQ
//        if (发送MQ 失败){
//            // 重新添加到待执行队列里
//        }

        // 发送MQ success,修改 状态为 TimeoutCenterStateEnum.SUCCESS
        Integer updateSuccessResult = timeoutTaskService.updateTaskStateLock(TimeoutCenterStateEnum.SUCCESS,
                TimeoutCenterStateEnum.EXECUTION, bizType, bizId);
        // TODO  考虑下 修改成功状态失败怎么办
    }
}
