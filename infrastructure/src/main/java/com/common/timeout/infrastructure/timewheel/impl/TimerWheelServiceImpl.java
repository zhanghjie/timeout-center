package com.common.timeout.infrastructure.timewheel.impl;

import com.alibaba.fastjson.JSON;
import com.common.timeout.infrastructure.threadpool.TCThreadPoolExecutor;
import com.common.timeout.infrastructure.timewheel.TimerWheelService;
import com.common.timeout.infrastructure.timewheel.vo.TimeWheel;
import com.common.timeout.infrastructure.timewheel.vo.TimerTask;
import com.common.timeout.infrastructure.timewheel.vo.TimerTaskEntry;
import com.common.timeout.infrastructure.timewheel.vo.TimerTaskList;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.*;

/**
 * TimerWheelServiceImpl
 * 功能描述：时间轮方法实现类
 *
 * @author zhanghaojie
 * @date 2022/8/18 10:46
 */
@Slf4j
@Service
public class TimerWheelServiceImpl implements TimerWheelService {
    /**
     * 底层时间轮
     */
    @Getter
    @Setter
    private TimeWheel timeWheel;

    /**
     * 一个Timer只有一个延时队列
     */
    private final DelayQueue<TimerTaskList> delayQueue = new DelayQueue<>();

    ThreadFactory workerThreadFactory = new ThreadFactoryBuilder()
            .setNameFormat("workerThread-pool-%d").build();
    /**
     * 过期任务执行线程
     */
    private TCThreadPoolExecutor workerThreadPool = new TCThreadPoolExecutor(8, 100,
            5, TimeUnit.SECONDS);

    ThreadFactory bossThreadFactory = new ThreadFactoryBuilder()
            .setNameFormat("bossThread-pool-%d").build();
    /**
     * 轮询delayQueue获取过期任务线程
     */
    private ExecutorService bossThreadPool = new ThreadPoolExecutor(1, 1,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(), bossThreadFactory);


    public TimerWheelServiceImpl() {
        this.timeWheel = new TimeWheel(1, 20, System.currentTimeMillis(), delayQueue);
        //20ms推动一次时间轮运转
        this.bossThreadPool.submit(() -> {
            for (; ; ) {
                this.advanceClock(5L);
            }
        });
    }


    public void addTimerTaskEntry(TimerTaskEntry entry) {
        if (!timeWheel.add(entry)) {
            //已经过期了
            TimerTask timerTask = entry.getTimerTask();
            log.info("=====任务:{} 已到期,准备执行============", timerTask.getBizId());
            workerThreadPool.submitTask(timerTask, timerTask.getOrder());
        }
    }

    @Override
    public void add(TimerTask timerTask) {
        log.info("=======添加任务开始====task:{}", JSON.toJSONString(timerTask));
        TimerTaskEntry entry = new TimerTaskEntry(timerTask, timerTask.getActionTime());
        timerTask.setTimerTaskEntry(entry);
        addTimerTaskEntry(entry);
    }

    /**
     * 推动指针运转获取过期任务
     *
     * @param timeout 时间间隔
     * @return
     */
    public synchronized void advanceClock(Long timeout) {
        try {
            TimerTaskList bucket = delayQueue.poll(timeout, TimeUnit.MILLISECONDS);
            if (bucket != null) {
                //推进时间
                timeWheel.advanceLock(bucket.getExpiration());
                //执行过期任务(包含降级)
                bucket.clear(this::addTimerTaskEntry);
            }
        } catch (InterruptedException e) {
            log.error("advanceClock error");
        }
    }

    @Override
    public void shutdown() {
        this.bossThreadPool.shutdown();
        this.workerThreadPool.shutdown();
        this.timeWheel = null;
    }
}
