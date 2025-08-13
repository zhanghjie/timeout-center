package com.common.timeout.api.rpc;

import com.common.timeout.api.dto.AddTimeoutTaskDTO;
import com.common.timeout.api.dto.TimeoutTaskVO;
import com.common.timeout.api.dto.WebResponse;

/**
 * TimeoutCenterRpcService
 * 功能描述: 超时中心RPC服务抽象接口，所有协议适配器都要实现此接口
 *
 * @author zhanghaojie
 * @date 2024/01/15
 */
public interface TimeoutCenterRpcService {
    
    /**
     * 查询超时中心任务
     *
     * @param bizType 业务类型
     * @param bizId   业务id
     * @return TimeoutTaskDTO
     */
    RpcResponse<TimeoutTaskVO> queryTimeoutTask(String bizType, String bizId);
    
    /**
     * 添加超时中心任务
     *
     * @param addTimeoutTaskDTO 添加超时中心任务对象
     * @return WebResponse
     */
    RpcResponse addTimeoutTask(AddTimeoutTaskDTO addTimeoutTaskDTO);
    
    /**
     * 取消超时中心任务
     *
     * @param bizType 业务类型
     * @param bizId   业务id
     * @return WebResponse
     */
    RpcResponse cancelTimeoutTask(String bizType, String bizId);
    
    /**
     * 获取当前RPC协议类型
     *
     * @return RPC协议类型
     */
    RpcProtocol getProtocol();
    
    /**
     * 健康检查
     *
     * @return 服务是否健康
     */
    RpcResponse<Boolean> healthCheck();
}
