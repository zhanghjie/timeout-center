package com.common.timeout.infrastructure.rpc.config;

import com.common.timeout.api.rpc.RpcProtocol;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * RpcProperties
 * 功能描述: RPC协议配置属性类
 *
 * @author zhanghaojie
 * @date 2024/01/15
 */
@Component
@ConfigurationProperties(prefix = "timeout-center.rpc")
public class RpcProperties {
    
    /**
     * 当前使用的RPC协议
     */
    private String protocol = "dubbo";
    
    /**
     * 是否启用多协议支持
     */
    private boolean multiProtocolEnabled = false;
    
    /**
     * 连接超时时间(毫秒)
     */
    private long connectTimeout = 5000L;
    
    /**
     * 读取超时时间(毫秒)
     */
    private long readTimeout = 30000L;
    
    /**
     * 重试次数
     */
    private int retryCount = 3;
    
    /**
     * 是否启用负载均衡
     */
    private boolean loadBalanceEnabled = true;
    
    /**
     * 负载均衡策略
     */
    private String loadBalanceStrategy = "round_robin";
    
    /**
     * Dubbo配置
     */
    private DubboConfig dubbo = new DubboConfig();
    
    /**
     * gRPC配置
     */
    private GrpcConfig grpc = new GrpcConfig();
    
    /**
     * Netty配置
     */
    private NettyConfig netty = new NettyConfig();
    
    /**
     * HTTP配置
     */
    private HttpConfig http = new HttpConfig();
    
    /**
     * Feign配置
     */
    private FeignConfig feign = new FeignConfig();
    
    public RpcProtocol getRpcProtocol() {
        return RpcProtocol.fromCode(protocol);
    }
    
    // Getters and Setters
    public String getProtocol() {
        return protocol;
    }
    
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }
    
    public boolean isMultiProtocolEnabled() {
        return multiProtocolEnabled;
    }
    
    public void setMultiProtocolEnabled(boolean multiProtocolEnabled) {
        this.multiProtocolEnabled = multiProtocolEnabled;
    }
    
    public long getConnectTimeout() {
        return connectTimeout;
    }
    
    public void setConnectTimeout(long connectTimeout) {
        this.connectTimeout = connectTimeout;
    }
    
    public long getReadTimeout() {
        return readTimeout;
    }
    
    public void setReadTimeout(long readTimeout) {
        this.readTimeout = readTimeout;
    }
    
    public int getRetryCount() {
        return retryCount;
    }
    
    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }
    
    public boolean isLoadBalanceEnabled() {
        return loadBalanceEnabled;
    }
    
    public void setLoadBalanceEnabled(boolean loadBalanceEnabled) {
        this.loadBalanceEnabled = loadBalanceEnabled;
    }
    
    public String getLoadBalanceStrategy() {
        return loadBalanceStrategy;
    }
    
    public void setLoadBalanceStrategy(String loadBalanceStrategy) {
        this.loadBalanceStrategy = loadBalanceStrategy;
    }
    
    public DubboConfig getDubbo() {
        return dubbo;
    }
    
    public void setDubbo(DubboConfig dubbo) {
        this.dubbo = dubbo;
    }
    
    public GrpcConfig getGrpc() {
        return grpc;
    }
    
    public void setGrpc(GrpcConfig grpc) {
        this.grpc = grpc;
    }
    
    public NettyConfig getNetty() {
        return netty;
    }
    
    public void setNetty(NettyConfig netty) {
        this.netty = netty;
    }
    
    public HttpConfig getHttp() {
        return http;
    }
    
    public void setHttp(HttpConfig http) {
        this.http = http;
    }
    
    public FeignConfig getFeign() {
        return feign;
    }
    
    public void setFeign(FeignConfig feign) {
        this.feign = feign;
    }
    
    /**
     * HTTP配置
     */
    public static class HttpConfig {
        private String baseUrl = "http://localhost:8080";
        private String contextPath = "/timeout-center";
        private int port = 8080;
        
        // Getters and Setters
        public String getBaseUrl() {
            return baseUrl;
        }
        
        public void setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
        }
        
        public String getContextPath() {
            return contextPath;
        }
        
        public void setContextPath(String contextPath) {
            this.contextPath = contextPath;
        }
        
        public int getPort() {
            return port;
        }
        
        public void setPort(int port) {
            this.port = port;
        }
    }
    
    /**
     * Feign配置
     */
    public static class FeignConfig {
        private String serviceUrl = "http://localhost:8080";
        private String serviceName = "timeout-center";
        private boolean hystrixEnabled = true;
        
        // Getters and Setters
        public String getServiceUrl() {
            return serviceUrl;
        }
        
        public void setServiceUrl(String serviceUrl) {
            this.serviceUrl = serviceUrl;
        }
        
        public String getServiceName() {
            return serviceName;
        }
        
        public void setServiceName(String serviceName) {
            this.serviceName = serviceName;
        }
        
        public boolean isHystrixEnabled() {
            return hystrixEnabled;
        }
        
        public void setHystrixEnabled(boolean hystrixEnabled) {
            this.hystrixEnabled = hystrixEnabled;
        }
    }
}
