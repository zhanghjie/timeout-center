package com.common.timeout.infrastructure.rpc.config;

/**
 * DubboConfig
 * 功能描述: Dubbo协议专用配置
 *
 * @author zhanghaojie
 * @date 2024/01/15
 */
public class DubboConfig {
    
    /**
     * 注册中心地址
     */
    private String registry = "zookeeper://localhost:2181";
    
    /**
     * 服务端口
     */
    private int port = 20880;
    
    /**
     * 协议名称
     */
    private String protocolName = "dubbo";
    
    /**
     * 应用名称
     */
    private String applicationName = "timeout-center";
    
    /**
     * 服务分组
     */
    private String group = "timeout-center";
    
    /**
     * 服务版本
     */
    private String version = "1.0.0";
    
    /**
     * 线程池大小
     */
    private int threads = 200;
    
    /**
     * 请求队列大小
     */
    private int queues = 0;
    
    /**
     * 序列化方式
     */
    private String serialization = "hessian2";
    
    /**
     * 集群容错策略
     */
    private String cluster = "failfast";
    
    /**
     * 负载均衡策略
     */
    private String loadbalance = "random";
    
    // Getters and Setters
    public String getRegistry() {
        return registry;
    }
    
    public void setRegistry(String registry) {
        this.registry = registry;
    }
    
    public int getPort() {
        return port;
    }
    
    public void setPort(int port) {
        this.port = port;
    }
    
    public String getProtocolName() {
        return protocolName;
    }
    
    public void setProtocolName(String protocolName) {
        this.protocolName = protocolName;
    }
    
    public String getApplicationName() {
        return applicationName;
    }
    
    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }
    
    public String getGroup() {
        return group;
    }
    
    public void setGroup(String group) {
        this.group = group;
    }
    
    public String getVersion() {
        return version;
    }
    
    public void setVersion(String version) {
        this.version = version;
    }
    
    public int getThreads() {
        return threads;
    }
    
    public void setThreads(int threads) {
        this.threads = threads;
    }
    
    public int getQueues() {
        return queues;
    }
    
    public void setQueues(int queues) {
        this.queues = queues;
    }
    
    public String getSerialization() {
        return serialization;
    }
    
    public void setSerialization(String serialization) {
        this.serialization = serialization;
    }
    
    public String getCluster() {
        return cluster;
    }
    
    public void setCluster(String cluster) {
        this.cluster = cluster;
    }
    
    public String getLoadbalance() {
        return loadbalance;
    }
    
    public void setLoadbalance(String loadbalance) {
        this.loadbalance = loadbalance;
    }
}
