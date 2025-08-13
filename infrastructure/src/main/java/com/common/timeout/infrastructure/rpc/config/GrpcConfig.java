package com.common.timeout.infrastructure.rpc.config;

/**
 * GrpcConfig
 * 功能描述: gRPC协议专用配置
 *
 * @author zhanghaojie
 * @date 2024/01/15
 */
public class GrpcConfig {
    
    /**
     * gRPC服务端口
     */
    private int port = 9090;
    
    /**
     * 服务器地址
     */
    private String host = "localhost";
    
    /**
     * 最大消息大小(字节)
     */
    private int maxMessageSize = 4 * 1024 * 1024; // 4MB
    
    /**
     * 最大头部大小(字节)
     */
    private int maxHeaderSize = 8192; // 8KB
    
    /**
     * 连接保活时间(秒)
     */
    private int keepAliveTime = 30;
    
    /**
     * 连接保活超时时间(秒)
     */
    private int keepAliveTimeout = 5;
    
    /**
     * 是否启用保活
     */
    private boolean keepAliveWithoutCalls = false;
    
    /**
     * 最大连接空闲时间(秒)
     */
    private int maxConnectionIdle = 30;
    
    /**
     * 最大连接年龄(秒)
     */
    private int maxConnectionAge = 30;
    
    /**
     * 是否启用TLS
     */
    private boolean tlsEnabled = false;
    
    /**
     * 证书文件路径
     */
    private String certChainFile;
    
    /**
     * 私钥文件路径
     */
    private String privateKeyFile;
    
    /**
     * 信任证书文件路径
     */
    private String trustCertCollectionFile;
    
    // Getters and Setters
    public int getPort() {
        return port;
    }
    
    public void setPort(int port) {
        this.port = port;
    }
    
    public String getHost() {
        return host;
    }
    
    public void setHost(String host) {
        this.host = host;
    }
    
    public int getMaxMessageSize() {
        return maxMessageSize;
    }
    
    public void setMaxMessageSize(int maxMessageSize) {
        this.maxMessageSize = maxMessageSize;
    }
    
    public int getMaxHeaderSize() {
        return maxHeaderSize;
    }
    
    public void setMaxHeaderSize(int maxHeaderSize) {
        this.maxHeaderSize = maxHeaderSize;
    }
    
    public int getKeepAliveTime() {
        return keepAliveTime;
    }
    
    public void setKeepAliveTime(int keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
    }
    
    public int getKeepAliveTimeout() {
        return keepAliveTimeout;
    }
    
    public void setKeepAliveTimeout(int keepAliveTimeout) {
        this.keepAliveTimeout = keepAliveTimeout;
    }
    
    public boolean isKeepAliveWithoutCalls() {
        return keepAliveWithoutCalls;
    }
    
    public void setKeepAliveWithoutCalls(boolean keepAliveWithoutCalls) {
        this.keepAliveWithoutCalls = keepAliveWithoutCalls;
    }
    
    public int getMaxConnectionIdle() {
        return maxConnectionIdle;
    }
    
    public void setMaxConnectionIdle(int maxConnectionIdle) {
        this.maxConnectionIdle = maxConnectionIdle;
    }
    
    public int getMaxConnectionAge() {
        return maxConnectionAge;
    }
    
    public void setMaxConnectionAge(int maxConnectionAge) {
        this.maxConnectionAge = maxConnectionAge;
    }
    
    public boolean isTlsEnabled() {
        return tlsEnabled;
    }
    
    public void setTlsEnabled(boolean tlsEnabled) {
        this.tlsEnabled = tlsEnabled;
    }
    
    public String getCertChainFile() {
        return certChainFile;
    }
    
    public void setCertChainFile(String certChainFile) {
        this.certChainFile = certChainFile;
    }
    
    public String getPrivateKeyFile() {
        return privateKeyFile;
    }
    
    public void setPrivateKeyFile(String privateKeyFile) {
        this.privateKeyFile = privateKeyFile;
    }
    
    public String getTrustCertCollectionFile() {
        return trustCertCollectionFile;
    }
    
    public void setTrustCertCollectionFile(String trustCertCollectionFile) {
        this.trustCertCollectionFile = trustCertCollectionFile;
    }
}
