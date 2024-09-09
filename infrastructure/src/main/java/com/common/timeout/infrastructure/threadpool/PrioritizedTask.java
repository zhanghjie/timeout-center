package com.common.timeout.infrastructure.threadpool;

// 自定义任务类，实现 Comparable 接口
public class PrioritizedTask implements Runnable, Comparable<PrioritizedTask> {
    private final Runnable task;
    private final int priority;

    public PrioritizedTask(Runnable task, int priority) {
        this.task = task;
        this.priority = priority;
    }

    @Override
    public void run() {
        task.run();
    }

    @Override
    public int compareTo(PrioritizedTask o) {
        return Integer.compare(o.priority, this.priority); // 优先级越高，排在越前面
    }
}