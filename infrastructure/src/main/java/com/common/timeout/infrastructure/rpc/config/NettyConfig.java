package com.common.timeout.infrastructure.rpc.config;

/**
 * NettyConfig
 * 功能描述: Netty协议专用配置
 *
 * @author zhanghaojie
 * @date 2024/01/15
 */
public class NettyConfig {
    
    /**
     * Netty服务端口
     */
    private int port = 8888;
    
    /**
     * 服务器地址
     */
    private String host = "0.0.0.0";
    
    /**
     * Boss线程数
     */
    private int bossThreads = 1;
    
    /**
     * Worker线程数
     */
    private int workerThreads = Runtime.getRuntime().availableProcessors() * 2;
    
    /**
     * 连接超时时间(毫秒)
     */
    private int connectTimeout = 5000;
    
    /**
     * SO_BACKLOG参数
     */
    private int backlog = 1024;
    
    /**
     * 发送缓冲区大小
     */
    private int sendBufferSize = 64 * 1024;
    
    /**
     * 接收缓冲区大小
     */
    private int receiveBufferSize = 64 * 1024;
    
    /**
     * 是否启用TCP_NODELAY
     */
    private boolean tcpNoDelay = true;
    
    /**
     * 是否启用SO_KEEPALIVE
     */
    private boolean keepAlive = true;
    
    /**
     * 是否启用SO_REUSEADDR
     */
    private boolean reuseAddress = true;
    
    /**
     * 心跳间隔时间(秒)
     */
    private int heartbeatInterval = 30;
    
    /**
     * 读空闲时间(秒)
     */
    private int readerIdleTime = 60;
    
    /**
     * 写空闲时间(秒)
     */
    private int writerIdleTime = 60;
    
    /**
     * 读写空闲时间(秒)
     */
    private int allIdleTime = 120;
    
    /**
     * 最大帧长度
     */
    private int maxFrameLength = 1024 * 1024; // 1MB
    
    /**
     * 序列化方式
     */
    private String serialization = "protobuf";
    
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
    
    public int getBossThreads() {
        return bossThreads;
    }
    
    public void setBossThreads(int bossThreads) {
        this.bossThreads = bossThreads;
    }
    
    public int getWorkerThreads() {
        return workerThreads;
    }
    
    public void setWorkerThreads(int workerThreads) {
        this.workerThreads = workerThreads;
    }
    
    public int getConnectTimeout() {
        return connectTimeout;
    }
    
    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }
    
    public int getBacklog() {
        return backlog;
    }
    
    public void setBacklog(int backlog) {
        this.backlog = backlog;
    }
    
    public int getSendBufferSize() {
        return sendBufferSize;
    }
    
    public void setSendBufferSize(int sendBufferSize) {
        this.sendBufferSize = sendBufferSize;
    }
    
    public int getReceiveBufferSize() {
        return receiveBufferSize;
    }
    
    public void setReceiveBufferSize(int receiveBufferSize) {
        this.receiveBufferSize = receiveBufferSize;
    }
    
    public boolean isTcpNoDelay() {
        return tcpNoDelay;
    }
    
    public void setTcpNoDelay(boolean tcpNoDelay) {
        this.tcpNoDelay = tcpNoDelay;
    }
    
    public boolean isKeepAlive() {
        return keepAlive;
    }
    
    public void setKeepAlive(boolean keepAlive) {
        this.keepAlive = keepAlive;
    }
    
    public boolean isReuseAddress() {
        return reuseAddress;
    }
    
    public void setReuseAddress(boolean reuseAddress) {
        this.reuseAddress = reuseAddress;
    }
    
    public int getHeartbeatInterval() {
        return heartbeatInterval;
    }
    
    public void setHeartbeatInterval(int heartbeatInterval) {
        this.heartbeatInterval = heartbeatInterval;
    }
    
    public int getReaderIdleTime() {
        return readerIdleTime;
    }
    
    public void setReaderIdleTime(int readerIdleTime) {
        this.readerIdleTime = readerIdleTime;
    }
    
    public int getWriterIdleTime() {
        return writerIdleTime;
    }
    
    public void setWriterIdleTime(int writerIdleTime) {
        this.writerIdleTime = writerIdleTime;
    }
    
    public int getAllIdleTime() {
        return allIdleTime;
    }
    
    public void setAllIdleTime(int allIdleTime) {
        this.allIdleTime = allIdleTime;
    }
    
    public int getMaxFrameLength() {
        return maxFrameLength;
    }
    
    public void setMaxFrameLength(int maxFrameLength) {
        this.maxFrameLength = maxFrameLength;
    }
    
    public String getSerialization() {
        return serialization;
    }
    
    public void setSerialization(String serialization) {
        this.serialization = serialization;
    }
}
