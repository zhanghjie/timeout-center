package com.common.timeout.client.task;


import com.alibaba.fastjson.JSON;
import com.common.timeout.client.db.model.TimeoutTaskDTO;

/**
 * ScheduledThreadPoolTask
 * 功能描述：延迟队列执行对象
 *
 * @author zhanghaojie
 * @date 2022/4/20 09:50
 */
public class ScheduledThreadPoolTask implements Runnable {


    private TimeoutTaskDTO timeoutTaskDTO;

    public ScheduledThreadPoolTask(TimeoutTaskDTO timeoutTaskDTO) {
        this.timeoutTaskDTO = timeoutTaskDTO;
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        System.out.println(JSON.toJSONString(timeoutTaskDTO));
        // 查询状态
        // 发送 MQ
        // 修改 状态
    }


}
