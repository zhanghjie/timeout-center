package com.common.timeout.api.rpc;

import java.io.Serializable;

/**
 * RpcResponse
 * 功能描述: 统一的RPC响应对象
 *
 * @author zhanghaojie
 * @date 2024/01/15
 */
public class RpcResponse<T> implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 请求ID
     */
    private String requestId;
    
    /**
     * 是否成功
     */
    private boolean success;
    
    /**
     * 响应数据
     */
    private T data;
    
    /**
     * 错误码
     */
    private String errorCode;
    
    /**
     * 错误信息
     */
    private String errorMessage;
    
    /**
     * 异常信息
     */
    private Throwable exception;
    
    /**
     * 响应时间戳
     */
    private long timestamp;
    
    public RpcResponse() {
        this.timestamp = System.currentTimeMillis();
    }
    
    public static <T> RpcResponse<T> success(T data) {
        RpcResponse<T> response = new RpcResponse<>();
        response.setSuccess(true);
        response.setData(data);
        return response;
    }
    
    public static <T> RpcResponse<T> success() {
        return success(null);
    }
    
    public static <T> RpcResponse<T> fail(String errorCode, String errorMessage) {
        RpcResponse<T> response = new RpcResponse<>();
        response.setSuccess(false);
        response.setErrorCode(errorCode);
        response.setErrorMessage(errorMessage);
        return response;
    }
    
    public static <T> RpcResponse<T> fail(String errorCode, String errorMessage, Throwable exception) {
        RpcResponse<T> response = fail(errorCode, errorMessage);
        response.setException(exception);
        return response;
    }
    
    // Getters and Setters
    public String getRequestId() {
        return requestId;
    }
    
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public T getData() {
        return data;
    }
    
    public void setData(T data) {
        this.data = data;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
    
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
    
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
    public Throwable getException() {
        return exception;
    }
    
    public void setException(Throwable exception) {
        this.exception = exception;
    }
    
    public long getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
