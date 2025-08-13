package com.common.timeout.api.rpc;

import java.io.Serializable;
import java.util.Map;

/**
 * RpcRequest
 * 功能描述: 统一的RPC请求对象
 *
 * @author zhanghaojie
 * @date 2024/01/15
 */
public class RpcRequest implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 请求ID
     */
    private String requestId;
    
    /**
     * 服务名称
     */
    private String serviceName;
    
    /**
     * 方法名称
     */
    private String methodName;
    
    /**
     * 参数类型
     */
    private Class<?>[] parameterTypes;
    
    /**
     * 参数值
     */
    private Object[] parameters;
    
    /**
     * 请求头信息
     */
    private Map<String, Object> headers;
    
    /**
     * 超时时间(毫秒)
     */
    private long timeout = 30000L;
    
    public RpcRequest() {
    }
    
    public RpcRequest(String serviceName, String methodName, Class<?>[] parameterTypes, Object[] parameters) {
        this.serviceName = serviceName;
        this.methodName = methodName;
        this.parameterTypes = parameterTypes;
        this.parameters = parameters;
    }
    
    // Getters and Setters
    public String getRequestId() {
        return requestId;
    }
    
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
    
    public String getServiceName() {
        return serviceName;
    }
    
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
    
    public String getMethodName() {
        return methodName;
    }
    
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
    
    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }
    
    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }
    
    public Object[] getParameters() {
        return parameters;
    }
    
    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }
    
    public Map<String, Object> getHeaders() {
        return headers;
    }
    
    public void setHeaders(Map<String, Object> headers) {
        this.headers = headers;
    }
    
    public long getTimeout() {
        return timeout;
    }
    
    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }
}
