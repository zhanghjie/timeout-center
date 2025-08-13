package com.common.timeout.infrastructure.rpc.adapter;

import com.alibaba.fastjson.JSON;
import com.common.timeout.api.dto.AddTimeoutTaskDTO;
import com.common.timeout.api.dto.TimeoutTaskVO;
import com.common.timeout.api.dto.WebResponse;
import com.common.timeout.api.rpc.RpcProtocol;
import com.common.timeout.api.rpc.RpcResponse;
import com.common.timeout.api.rpc.TimeoutCenterRpcService;
import com.common.timeout.infrastructure.rpc.config.RpcProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * HttpTimeoutServiceAdapter
 * 功能描述: HTTP协议适配器实现
 *
 * @author zhanghaojie
 * @date 2024/01/15
 */
@Slf4j
@Component
public class HttpTimeoutServiceAdapter implements TimeoutCenterRpcService {
    
    @Autowired
    private RpcProperties rpcProperties;
    
    private final RestTemplate restTemplate;
    
    public HttpTimeoutServiceAdapter() {
        this.restTemplate = new RestTemplate();
    }
    
    private String getBaseUrl() {
        return rpcProperties.getHttp().getBaseUrl() + rpcProperties.getHttp().getContextPath();
    }
    
    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("User-Agent", "timeout-center-http-client");
        return headers;
    }
    
    @Override
    public RpcResponse<TimeoutTaskVO> queryTimeoutTask(String bizType, String bizId) {
        try {
            log.debug("HTTP协议查询超时任务: bizType={}, bizId={}", bizType, bizId);
            
            String url = getBaseUrl() + "/api/timeout-task/query?bizType={bizType}&bizId={bizId}";
            HttpEntity<String> entity = new HttpEntity<>(createHeaders());
            
            ResponseEntity<WebResponse<TimeoutTaskVO>> response = restTemplate.exchange(
                url, 
                HttpMethod.GET, 
                entity, 
                new ParameterizedTypeReference<WebResponse<TimeoutTaskVO>>() {},
                bizType, 
                bizId
            );
            
            WebResponse<TimeoutTaskVO> webResponse = response.getBody();
            if (webResponse != null && webResponse.getIsSuccess()) {
                return RpcResponse.success(webResponse.getData());
            } else {
                return RpcResponse.fail(webResponse.getCode(), webResponse.getMsg());
            }
        } catch (Exception e) {
            log.error("HTTP协议查询超时任务失败: bizType={}, bizId={}", bizType, bizId, e);
            return RpcResponse.fail("HTTP_ERROR", "HTTP调用失败: " + e.getMessage(), e);
        }
    }
    
    @Override
    public RpcResponse addTimeoutTask(AddTimeoutTaskDTO addTimeoutTaskDTO) {
        try {
            log.debug("HTTP协议添加超时任务: {}", addTimeoutTaskDTO.getBizId());
            
            String url = getBaseUrl() + "/api/timeout-task/add";
            HttpEntity<String> entity = new HttpEntity<>(JSON.toJSONString(addTimeoutTaskDTO), createHeaders());
            
            ResponseEntity<WebResponse> response = restTemplate.exchange(
                url, 
                HttpMethod.POST, 
                entity, 
                WebResponse.class
            );
            
            return RpcResponse.success(response.getBody());
        } catch (Exception e) {
            log.error("HTTP协议添加超时任务失败: {}", addTimeoutTaskDTO.getBizId(), e);
            return RpcResponse.fail("HTTP_ERROR", "HTTP调用失败: " + e.getMessage(), e);
        }
    }
    
    @Override
    public RpcResponse cancelTimeoutTask(String bizType, String bizId) {
        try {
            log.debug("HTTP协议取消超时任务: bizType={}, bizId={}", bizType, bizId);
            
            String url = getBaseUrl() + "/api/timeout-task/cancel?bizType={bizType}&bizId={bizId}";
            HttpEntity<String> entity = new HttpEntity<>(createHeaders());
            
            ResponseEntity<WebResponse> response = restTemplate.exchange(
                url, 
                HttpMethod.DELETE, 
                entity, 
                WebResponse.class,
                bizType, 
                bizId
            );
            
            return RpcResponse.success(response.getBody());
        } catch (Exception e) {
            log.error("HTTP协议取消超时任务失败: bizType={}, bizId={}", bizType, bizId, e);
            return RpcResponse.fail("HTTP_ERROR", "HTTP调用失败: " + e.getMessage(), e);
        }
    }
    
    @Override
    public RpcProtocol getProtocol() {
        return RpcProtocol.HTTP;
    }
    
    @Override
    public RpcResponse<Boolean> healthCheck() {
        try {
            String url = getBaseUrl() + "/api/health";
            HttpEntity<String> entity = new HttpEntity<>(createHeaders());
            
            ResponseEntity<String> response = restTemplate.exchange(
                url, 
                HttpMethod.GET, 
                entity, 
                String.class
            );
            
            return RpcResponse.success(response.getStatusCode().is2xxSuccessful());
        } catch (Exception e) {
            log.warn("HTTP协议健康检查失败", e);
            return RpcResponse.fail("HEALTH_CHECK_FAILED", "HTTP服务健康检查失败", e);
        }
    }
}
