package com.common.timeout.client.scheduled;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * PrepareQueueTask
 * 功能描述：执行任务定时任务
 *
 * @author zhanghaojie
 * @date 2022/3/17 17:47
 */
public class PrepareQueueTask {


    /**
     * 功能描述: 执行定时任务，查询待发送的数据
     *
     * @author zhanghaojie
     * @date 2022/3/17 18:02
     */
    @Scheduled(fixedDelay = 100)
    @Async
    public void execute() {
        // todo
        //  获取当前时间戳的prepareTask
        //  ack mq 确定发送状态
        //  修改成功数据的DB
        //  发送不成功的记录到prepareQueue
    }
}
