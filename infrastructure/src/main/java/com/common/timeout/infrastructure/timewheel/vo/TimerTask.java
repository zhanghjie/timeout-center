package com.common.timeout.infrastructure.timewheel.vo;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * TimerTask
 * 功能描述：TODO
 *
 * @author zhanghaojie
 * @date 2022/8/18 10:44
 */
@Data
@Slf4j
public class TimerTask implements Runnable {

    /**
     * 延时时间
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


    public TimerTask(String bizType, String bizId, long actionTime) {
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
        System.out.println("执行了定时任务");
    }
}
