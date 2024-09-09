package com.common.timeout.infrastructure.threadpool;

import java.util.concurrent.PriorityBlockingQueue;

// 自定义任务队列
public class PriorityTaskQueue extends PriorityBlockingQueue<Runnable> {


    @Override
    public boolean offer(Runnable r) {
        if (r instanceof PrioritizedTask) {
            return super.offer(r);
        } else {
            // 默认情况下，将普通任务放在队列的末尾
            return super.offer(new PrioritizedTask(r, Integer.MIN_VALUE));
        }
    }
}