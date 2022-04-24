package com.common.timeout.client.task;


import com.common.timeout.client.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * ScheduledThreadPoolTask
 * 功能描述：延迟队列执行对象
 *
 * @author zhanghaojie
 * @date 2022/4/20 09:50
 */
@Slf4j
public class ScheduledThreadPoolTask implements Runnable {

    @Autowired
    private TaskService taskService;

    /**
     * 业务id，一般为关联的主订单或子订单id
     * 同一bizType下不可重复
     */
    private String bizId;

    /**
     * 业务类型
     */
    private String bizType;


    public ScheduledThreadPoolTask(String bizId, String bizType) {
        this.bizId = bizId;
        this.bizType = bizType;
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
        taskService.doTask(bizType, bizId);
    }


}
