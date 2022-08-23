package com.common.timeout.client.tools.timewheel;

import com.common.timeout.client.tools.timewheel.vo.TimerTask;

/**
 * TimerWheelService
 * 功能描述：TODO
 *
 * @author zhanghaojie
 * @date 2022/8/18 10:45
 */
public interface TimerWheelService {

    /**
     * 添加一个新任务
     *
     * @param timerTask
     */
    void add(TimerTask timerTask);


    /**
     * 推动指针
     *
     * @param timeout
     */
    void advanceClock(Long timeout);

    /**
     * 等待执行的任务
     *
     * @return
     */
    int size();

    /**
     * 关闭服务,剩下的无法被执行
     */
    void shutdown();
}
