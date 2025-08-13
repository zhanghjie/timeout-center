package com.common.timeout.infrastructure.rpc.adapter;

import com.common.timeout.api.TimeoutCenterService;
import com.common.timeout.api.dto.AddTimeoutTaskDTO;
import com.common.timeout.api.dto.TimeoutTaskVO;
import com.common.timeout.api.dto.WebResponse;
import com.common.timeout.api.rpc.RpcProtocol;
import com.common.timeout.api.rpc.RpcResponse;
import com.common.timeout.api.rpc.TimeoutCenterRpcService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * DubboTimeoutServiceAdapter
 * 功能描述: Dubbo协议适配器实现
 *
 * @author zhanghaojie
 * @date 2024/01/15
 */
@Slf4j
@Component
public class DubboTimeoutServiceAdapter implements TimeoutCenterRpcService {

    @Autowired
    private TimeoutCenterService timeoutCenterService;

    @Override
    public RpcResponse<TimeoutTaskVO> queryTimeoutTask(String bizType, String bizId) {
        try {
            log.debug("Dubbo协议查询超时任务: bizType={}, bizId={}", bizType, bizId);
            WebResponse<TimeoutTaskVO> response = timeoutCenterService.queryTimeoutTask(bizType, bizId);

            if (response.getIsSuccess()) {
                return RpcResponse.success(response.getData());
            } else {
                return RpcResponse.fail(response.getCode(), response.getCode());
            }
        } catch (Exception e) {
            log.error("Dubbo协议查询超时任务失败: bizType={}, bizId={}", bizType, bizId, e);
            return RpcResponse.fail("DUBBO_ERROR", "Dubbo调用失败: " + e.getMessage(), e);
        }
    }

    @Override
    public RpcResponse<WebResponse> addTimeoutTask(AddTimeoutTaskDTO addTimeoutTaskDTO) {
        try {
            log.debug("Dubbo协议添加超时任务: {}", addTimeoutTaskDTO.getBizId());
            WebResponse response = timeoutCenterService.addTimeoutTask(addTimeoutTaskDTO);
            return RpcResponse.success(response);
        } catch (Exception e) {
            log.error("Dubbo协议添加超时任务失败: {}", addTimeoutTaskDTO.getBizId(), e);
            return RpcResponse.fail("DUBBO_ERROR", "Dubbo调用失败: " + e.getMessage(), e);
        }
    }

    @Override
    public RpcResponse<WebResponse> cancelTimeoutTask(String bizType, String bizId) {
        try {
            log.debug("Dubbo协议取消超时任务: bizType={}, bizId={}", bizType, bizId);
            WebResponse response = timeoutCenterService.cancelTimeoutTask(bizType, bizId);
            return RpcResponse.success(response);
        } catch (Exception e) {
            log.error("Dubbo协议取消超时任务失败: bizType={}, bizId={}", bizType, bizId, e);
            return RpcResponse.fail("DUBBO_ERROR", "Dubbo调用失败: " + e.getMessage(), e);
        }
    }

    @Override
    public RpcProtocol getProtocol() {
        return RpcProtocol.DUBBO;
    }

    @Override
    public RpcResponse<Boolean> healthCheck() {
        try {
            // 通过简单的查询操作来检查服务健康状态
            timeoutCenterService.queryTimeoutTask("health-check", "test");
            return RpcResponse.success(true);
        } catch (Exception e) {
            log.warn("Dubbo协议健康检查失败", e);
            return RpcResponse.fail("HEALTH_CHECK_FAILED", "Dubbo服务健康检查失败", e);
        }
    }
}
