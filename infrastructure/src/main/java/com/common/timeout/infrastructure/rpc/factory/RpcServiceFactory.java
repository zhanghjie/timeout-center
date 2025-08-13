package com.common.timeout.infrastructure.rpc.factory;

import com.common.timeout.api.rpc.RpcProtocol;
import com.common.timeout.api.rpc.TimeoutCenterRpcService;
import com.common.timeout.infrastructure.rpc.adapter.DubboTimeoutServiceAdapter;
import com.common.timeout.infrastructure.rpc.adapter.FeignTimeoutServiceAdapter;
import com.common.timeout.infrastructure.rpc.adapter.GrpcTimeoutServiceAdapter;
import com.common.timeout.infrastructure.rpc.adapter.HttpTimeoutServiceAdapter;
import com.common.timeout.infrastructure.rpc.adapter.NettyTimeoutServiceAdapter;
import com.common.timeout.infrastructure.rpc.config.RpcProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * RpcServiceFactory
 * 功能描述: RPC服务工厂，根据配置动态创建对应协议的服务实例
 *
 * @author zhanghaojie
 * @date 2024/01/15
 */
@Slf4j
@Component
public class RpcServiceFactory {
    
    @Autowired
    private RpcProperties rpcProperties;
    
    @Autowired
    private DubboTimeoutServiceAdapter dubboAdapter;
    
    @Autowired
    private FeignTimeoutServiceAdapter feignAdapter;
    
    @Autowired
    private GrpcTimeoutServiceAdapter grpcAdapter;
    
    @Autowired
    private HttpTimeoutServiceAdapter httpAdapter;
    
    @Autowired
    private NettyTimeoutServiceAdapter nettyAdapter;
    
    private final Map<RpcProtocol, TimeoutCenterRpcService> serviceMap = new HashMap<>();
    
    @PostConstruct
    public void init() {
        // 注册所有协议适配器
        serviceMap.put(RpcProtocol.DUBBO, dubboAdapter);
        serviceMap.put(RpcProtocol.FEIGN, feignAdapter);
        serviceMap.put(RpcProtocol.GRPC, grpcAdapter);
        serviceMap.put(RpcProtocol.HTTP, httpAdapter);
        serviceMap.put(RpcProtocol.NETTY, nettyAdapter);
        
        log.info("RPC服务工厂初始化完成，当前协议: {}", rpcProperties.getProtocol());
    }
    
    /**
     * 获取当前配置的RPC服务实例
     *
     * @return RPC服务实例
     */
    public TimeoutCenterRpcService getCurrentService() {
        RpcProtocol protocol = rpcProperties.getRpcProtocol();
        return getService(protocol);
    }
    
    /**
     * 根据协议类型获取RPC服务实例
     *
     * @param protocol 协议类型
     * @return RPC服务实例
     */
    public TimeoutCenterRpcService getService(RpcProtocol protocol) {
        TimeoutCenterRpcService service = serviceMap.get(protocol);
        if (service == null) {
            throw new IllegalArgumentException("不支持的RPC协议: " + protocol);
        }
        return service;
    }
    
    /**
     * 获取所有可用的RPC服务实例
     *
     * @return 所有RPC服务实例
     */
    public Map<RpcProtocol, TimeoutCenterRpcService> getAllServices() {
        return new HashMap<>(serviceMap);
    }
    
    /**
     * 动态切换RPC协议
     *
     * @param protocol 新的协议类型
     */
    public void switchProtocol(RpcProtocol protocol) {
        if (!serviceMap.containsKey(protocol)) {
            throw new IllegalArgumentException("不支持的RPC协议: " + protocol);
        }
        
        rpcProperties.setProtocol(protocol.getCode());
        log.info("RPC协议已切换为: {}", protocol);
    }
    
    /**
     * 检查协议是否可用
     *
     * @param protocol 协议类型
     * @return 是否可用
     */
    public boolean isProtocolAvailable(RpcProtocol protocol) {
        TimeoutCenterRpcService service = serviceMap.get(protocol);
        if (service == null) {
            return false;
        }
        
        try {
            return service.healthCheck().isSuccess();
        } catch (Exception e) {
            log.warn("协议 {} 健康检查失败: {}", protocol, e.getMessage());
            return false;
        }
    }
}
