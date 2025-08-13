package com.common.timeout.api.rpc;

/**
 * RpcProtocol
 * 功能描述: RPC协议枚举
 *
 * @author zhanghaojie
 * @date 2024/01/15
 */
public enum RpcProtocol {
    
    /**
     * Dubbo协议
     */
    DUBBO("dubbo", "Apache Dubbo RPC协议"),
    
    /**
     * Feign协议 (Spring Cloud)
     */
    FEIGN("feign", "Spring Cloud Feign HTTP协议"),
    
    /**
     * gRPC协议
     */
    GRPC("grpc", "Google gRPC协议"),
    
    /**
     * HTTP协议
     */
    HTTP("http", "标准HTTP协议"),
    
    /**
     * Netty协议
     */
    NETTY("netty", "基于Netty的自定义协议");
    
    private final String code;
    private final String description;
    
    RpcProtocol(String code, String description) {
        this.code = code;
        this.description = description;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getDescription() {
        return description;
    }
    
    public static RpcProtocol fromCode(String code) {
        for (RpcProtocol protocol : values()) {
            if (protocol.getCode().equals(code)) {
                return protocol;
            }
        }
        throw new IllegalArgumentException("不支持的RPC协议: " + code);
    }
}
