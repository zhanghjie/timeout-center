package com.common.timeout.client.scheduled;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * StoreQueueTask
 * 功能描述：待执行任务定时任务
 *
 * @author zhanghaojie
 * @date 2022/3/17 17:47
 */
public class StoreQueueTask {


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
        //  先获取所有的类型 然后递归查询redis 获取其中的bizId
        //  从db获取出详细数据
        //  发送mq
        //  记录到prepareQueue
    }
}
