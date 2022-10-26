package com.common.timeout.infrastructure.db.model;

import lombok.Data;

/**
 * TypeMangerDTO
 * 功能描述：超时任务 类型管理类
 *
 * @author zhanghaojie
 * @date 2022/3/17 15:22
 */
@Data
public class TaskTypeMangerDTO {
    // id
    private String id;
    // 业务类型
    private String bizType;
    // 业务名称
    private String bizName;
    // 责任人
    private String responsible;
    // 超时任务发送mq的topic
    private String mqTopic;
    // 创建时间
    private Long createTime;
    // 修改时间
    private Long updateTime;

    public TaskTypeMangerDTO() {
    }


    public TaskTypeMangerDTO(String bizType) {
        this.bizType = bizType;
    }


}
