package com.common.timeout.infrastructure.timewheel.vo;

import lombok.Data;

/**
 * TimerTaskEntry
 * 功能描述：TODO
 *
 * @author zhanghaojie
 * @date 2022/8/18 10:43
 */
@Data
public class TimerTaskEntry implements Comparable<TimerTaskEntry> {
    private TimerTask timerTask;
    /**
     * 期望执行时间
     */
    private Long expireMs;
    volatile TimerTaskList timedTaskList;
    /** 当前对象在链表下一个元素的位置*/
    TimerTaskEntry next;
    /** 当前对象在链表上一个元素的位置*/
    TimerTaskEntry prev;

    public TimerTaskEntry(TimerTask timedTask, Long expireMs) {
        this.timerTask = timedTask;
        this.expireMs =  expireMs;
        this.next = null;
        this.prev = null;
    }

    void remove() {
        TimerTaskList currentList = timedTaskList;
        while (currentList != null) {
            currentList.remove(this);
            currentList = timedTaskList;
        }
    }

    @Override
    public int compareTo(TimerTaskEntry o) {
        return ((int) (this.expireMs - o.expireMs));
    }
}
