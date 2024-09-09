package com.common.timeout.infrastructure.timewheel;

import com.common.timeout.infrastructure.timewheel.vo.TimerTask;

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
     * 关闭服务,剩下的无法被执行
     */
    void shutdown();
}
