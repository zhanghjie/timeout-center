package com.quhuhu.common.timeout.dto;


import java.io.Serializable;

/**
 * TimeoutTaskDTO
 * 功能描述: 超时中心任务对象
 *
 * @author zhanghaojie
 * @date 2021/12/13 17:58
 */
public class AddTimeoutTaskDTO implements Serializable {

    private static final long serialVersionUID = -1;

    /**
     * 业务类型
     */
    private String bizType;

    /**
     * 业务唯一id,同一个BizType下不允许重复
     */
    private String bizId;

    /**
     * 期望执行时间 时间戳-秒级
     */
    private Long actionTime;

    /**
     * 任务数据
     */
    private String data;


    @Override
    public String toString() {
        return "TimeoutTaskDTO{" +
                ", bizType='" + bizType + '\'' +
                ", bizId='" + bizId + '\'' +
                ", data='" + data + '\'' +
                ", actionTime='" + actionTime + '\'' +
                '}';
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public Long getActionTime() {
        return actionTime;
    }

    public void setActionTime(Long actionTime) {
        this.actionTime = actionTime;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
