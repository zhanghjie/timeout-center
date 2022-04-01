package com.common.timeout.api.dto;

import com.common.timeout.api.enums.TimeoutCenterStateEnum;

import java.io.Serializable;

/**
 * TimeoutTaskDTO
 * 功能描述: 超时中心任务对象
 *
 * @author zhanghaojie
 * @date 2021/12/13 17:58
 */
public class TimeoutTaskVO implements Serializable {

    private static final long serialVersionUID = -1;

    /**
     * 任务Id
     */
    private String taskId;

    /**
     * 业务类型
     */
    private String bizType;

    /**
     * 业务唯一id,同一个BizType下不允许重复
     */
    private String bizId;

    /**
     * 超时中心状态
     * <p>
     * {@link TimeoutCenterStateEnum}
     */
    private Integer state;

    /**
     * 任务数据
     */
    private String data;

    /**
     * 任务创建时间
     */
    private String createTime;

    /**
     * 任务修改时间
     */
    private String updateTime;

    @Override
    public String toString() {
        return "TimeoutTaskDTO{" +
                "taskId='" + taskId + '\'' +
                ", bizType='" + bizType + '\'' +
                ", bizId='" + bizId + '\'' +
                ", state=" + state +
                ", data='" + data + '\'' +
                ", createTime='" + createTime + '\'' +
                ", updateTime='" + updateTime + '\'' +
                '}';
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
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

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
