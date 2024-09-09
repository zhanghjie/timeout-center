package com.common.timeout.domain.tread;

import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.Callable;

/**
 * NewTask
 * 功能描述：NewTask
 *
 * @author zhanghaojie
 * @date 2023/5/8 10:04
 */
public abstract class NewAbstractTask implements Callable<Object>, TaskOrder {
    /**
     * 任务取消标志位
     */
    @Setter
    @Getter
    private CancellationToken cancellationToken;

    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * @return computed result
     * @throws Exception if unable to compute a result
     */
    @Override
    public Object call() throws Exception {
        return toDoSomeThing();
    }


    /**
     * 业务执行代码
     */
    public abstract Object toDoSomeThing();
}
