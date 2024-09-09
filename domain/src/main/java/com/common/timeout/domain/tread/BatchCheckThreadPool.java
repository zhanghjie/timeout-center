package com.common.timeout.domain.tread;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * BatchThreadPool
 * 功能描述：批量的校验任务是否成功
 *
 * @author zhanghaojie
 * @date 2023/5/7 17:59
 */
public class BatchCheckThreadPool {
    private final ExecutorService executor;

    public BatchCheckThreadPool() {
        this.executor = Executors.newFixedThreadPool(10);
    }

    /**
     * 该方法返回的是批量任务的执行结果
     */
    public Boolean submit(List<NewAbstractTask> tasks) {
        CancellationToken cancellationToken = new CancellationToken();
        List<Future<Object>> futures;
        sortByOrder(tasks);
        for (NewAbstractTask task : tasks) {
            task.setCancellationToken(cancellationToken);

        }
        try {
            futures = executor.invokeAll(tasks);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return getaFutureList(cancellationToken, futures);
    }

    /**
     * 该方法返回的是批量任务的执行结果
     * 会根据配置的timeOutTime，规定任务的最长执行时间，如果超时则，会中断线程
     * 因为设置超时时间会增加开销，所以不建议如此做
     *
     * @param tasks       任务集
     * @param timeOutTime 超时时间
     * @param timeUnit    时间类型
     */
    public Boolean submit(List<NewAbstractTask> tasks, Integer timeOutTime, TimeUnit timeUnit) {
        CancellationToken cancellationToken = new CancellationToken();
        List<Future<Object>> futures;
        sortByOrder(tasks);
        for (NewAbstractTask task : tasks) {
            task.setCancellationToken(cancellationToken);
        }
        try {
            futures = executor.invokeAll(tasks, timeOutTime, timeUnit);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return getaFutureList(cancellationToken, futures);
    }



    @NotNull
    private Boolean getaFutureList(CancellationToken cancellationToken, List<Future<Object>> futures) {
        for (Future future : futures) {
            try {
                // 这里阻塞住的是执行时间最长的线程
                // 所以每个TASK,都应该继承order
                future.get();
            } catch (Exception ex) {
                System.out.println("执行过程发生异常，msg:" + ex.getMessage());
                System.out.println("线程组判断异常返回");
                return false;
            }
            if (cancellationToken.isCancelled()) {
                System.out.println("线程组判断正常返回");
                return false;
            }
        }
        return true;
    }


    private void sortByOrder(List<NewAbstractTask> list) {
        list.sort((o1, o2) -> {
            int order1 = o1.order();
            int order2 = o2.order();
            return Integer.compare(order1, order2);
        });
    }
}
