package com.common.timeout.infrastructure.threadpool;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 优先级线程池执行器类，继承自ThreadPoolExecutor，实现了自定义优先级的任务调度。
 * 通过定制任务的优先级，可以更灵活地控制任务的执行顺序，确保高优先级任务能够得到优先处理。
 */
public class TCThreadPoolExecutor extends ThreadPoolExecutor {

    /**
     * 构造函数，初始化优先级线程池执行器。
     *
     * @param corePoolSize 核心线程数，即始终存在的线程数。
     * @param maximumPoolSize 最大线程数，线程池能够容纳的最大线程数量。
     * @param keepAliveTime 空闲线程的存活时间，超过此时间空闲线程将被终止。
     * @param unit 时间单位，用于度量keepAliveTime。
     * 使用自定义的优先级任务队列PriorityTaskQueue来管理任务，确保任务按照优先级执行。
     */
    public TCThreadPoolExecutor(int corePoolSize, int maximumPoolSize, int keepAliveTime, TimeUnit unit) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, new PriorityTaskQueue());
    }


    /**
     * 提交一个具有优先级的任務。
     *
     * @param task 要执行的任务，必须实现Runnable接口。
     * @param priority 任务的优先级，用于决定任务的执行顺序。
     * 将任务包装成PrioritizedTask对象，PrioritizedTask实现了PriorityQueue接口，从而支持优先级比较。
     */
    public void submitTask(Runnable task, int priority) {
        super.execute(new PrioritizedTask(task, priority));
    }
}
