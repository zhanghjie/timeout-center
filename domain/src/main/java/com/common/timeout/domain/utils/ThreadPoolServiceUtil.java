package com.common.timeout.domain.utils;

import com.common.timeout.domain.tread.BatchCheckThreadPool;
import com.common.timeout.domain.tread.ExceptionTask;
import com.common.timeout.domain.tread.NewAbstractTask;
import com.common.timeout.domain.tread.NewTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * ThreadPoolServiceUtil
 * 功能描述：线程池创建工具
 *
 * @author zhanghaojie
 * @date 2023/5/7 17:54
 */
public class ThreadPoolServiceUtil {

    static BatchCheckThreadPool batchThreadPool = new BatchCheckThreadPool();


    public static void main(String[] args) throws Throwable {
        List<NewAbstractTask> runnableList = new ArrayList<>();
        runnableList.add(new NewTask());
        runnableList.add(new ExceptionTask());
        batchThreadPool.submit(runnableList, 300, TimeUnit.MILLISECONDS);
    }


}
