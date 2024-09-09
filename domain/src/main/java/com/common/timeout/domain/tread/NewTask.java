package com.common.timeout.domain.tread;

/**
 * NewTask
 * 功能描述：NewTask
 *
 * @author zhanghaojie
 * @date 2023/5/8 10:07
 */
public class NewTask extends NewAbstractTask {

    @Override
    public Boolean toDoSomeThing() {
        Long start = System.currentTimeMillis();
        try {
            System.out.println("睡眠开始");
            Thread.sleep(100);
            System.out.println("睡眠完成" + (System.currentTimeMillis() - start));
        } catch (Exception ex) {
            System.out.println("异常时，接口执行了" + (System.currentTimeMillis() - start));
            System.out.println("发生异常了");
        }
        return false;
    }

    /**
     * The smaller the first implementation
     *
     * @return
     */
    @Override
    public int order() {
        return 1;
    }
}
