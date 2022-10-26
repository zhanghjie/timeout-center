package com.common.timeout.api;


import com.common.timeout.api.dto.AddTimeoutTaskDTO;
import com.common.timeout.api.dto.TimeoutTaskVO;
import com.common.timeout.api.dto.WebResponse;

/**
 * TimeoutCenterService
 * 功能描述: 超时中心任务服务
 *
 * @author zhanghaojie
 * @date 2021/12/13 17:57
 */
public interface TimeoutCenterService {

    /**
     * 查询超时中心任务
     *
     * @param bizType 业务类型
     * @param bizId   业务id
     * @return TimeoutTaskDTO
     * @author zhanghaojie
     * @date 2021/12/13 18:49
     */
    WebResponse<TimeoutTaskVO> queryTimeoutTask(String bizType, String bizId);

    /**
     * 添加超时中心任务
     *
     * @param addTimeoutTaskDTO 添加超时中心任务对象
     * @return TimeoutTaskDTO
     * @author zhanghaojie
     * @date 2021/12/13 18:49
     */
    WebResponse addTimeoutTask(AddTimeoutTaskDTO addTimeoutTaskDTO);

    /**
     * 取消超时中心任务
     *
     * @param bizType 业务类型
     * @param bizId   业务id
     * @author zhanghaojie
     * @date 2021/12/13 18:49
     */
    WebResponse cancelTimeoutTask(String bizType, String bizId);
}
