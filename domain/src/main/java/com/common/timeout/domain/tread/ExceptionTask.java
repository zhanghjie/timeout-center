package com.common.timeout.domain.tread;

/**
 * NewTask
 * 功能描述：NewTask
 *
 * @author zhanghaojie
 * @date 2023/5/8 10:07
 */
public class ExceptionTask extends NewAbstractTask {

    @Override
    public Boolean toDoSomeThing() {
        System.out.println(this.getCancellationToken().toString());
        System.out.println("异常线程执行");
        this.getCancellationToken().cancel();
        System.out.println("异常线程执行-" + this.getCancellationToken().isCancelled());
        throw new RuntimeException("直接报错");
    }

    /**
     * The smaller the first implementation
     *
     * @return
     */
    @Override
    public int order() {
        return 0;
    }
}
