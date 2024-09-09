package com.common.timeout.infrastructure.timewheel.vo;

import com.common.timeout.infrastructure.threadpool.PrioritizedTask;
import lombok.Data;

/**
 * TimerTask
 * 功能描述：TODO
 *
 * @author zhanghaojie
 * @date 2022/8/18 10:44
 */
@Data
public class TimerTask implements Runnable {

    /**
     * 期待执行时间，毫秒
     */
    private Long actionTime;

    /**
     * 业务类型
     */
    private String bizType;
    /**
     * 业务id，一般为关联的主订单或子订单id
     * 同一bizType下不可重复
     */
    private String bizId;

    /**
     * 任务所在的entry
     */
    private TimerTaskEntry timerTaskEntry;

    /**
     * 任务所在的entry
     */
    private Integer entryId;

    private Integer order;


    public TimerTask(String bizType, String bizId, Long actionTime, Integer order) {
        this.order = order;
        this.bizType = bizType;
        this.bizId = bizId;
        this.actionTime = actionTime;
        this.timerTaskEntry = null;
    }

    public synchronized void setTimerTaskEntry(TimerTaskEntry entry) {
        // 如果这个timeTask已经被一个已存在的TimerTaskEntry持有,先移除一个
        if (timerTaskEntry != null && timerTaskEntry != entry) {
            timerTaskEntry.remove();
        }
        timerTaskEntry = entry;
    }


    @Override
    public void run() {
        // 根据 BizType 和 BizId 从数据库查询任务
        System.out.println("执行了定时任务,当前时间:" + System.currentTimeMillis() + "bizType:" + this.bizType + ",bizId" + bizId);
    }

    @Override
    public String toString() {
        return "TimerTask{" +
                "actionTime=" + actionTime +
                ", bizType='" + bizType + '\'' +
                ", bizId='" + bizId + '\'' +
                ", timerTaskEntry=" + timerTaskEntry +
                '}';
    }
}
