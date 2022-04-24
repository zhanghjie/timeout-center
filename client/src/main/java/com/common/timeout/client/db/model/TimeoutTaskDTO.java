package com.common.timeout.client.db.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import qunar.annotation.NotNull;

import java.util.Date;

/**
 * 超时中心任务表
 */
@Data
@TableName("timeout_task")
public class TimeoutTaskDTO {
    /**
     * 主键
     */
    private Long id;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 修改时间
     */
    private Long updateTime;

    /**
     * 业务id，一般为关联的主订单或子订单id
     * 同一bizType下不可重复
     */
    private String bizId;

    /**
     * 业务类型
     */
    private String bizType;

    /**
     * 超时任务状态（0待处理，2已处理，3取消）
     */
    private Integer state;

    /**
     * 超时任务期望执行时间 级时间戳
     */
    private Long actionTime;

    /**
     * 任务数据
     */
    private String data;

    /**
     * 任务版本
     */
    private Integer version;
}