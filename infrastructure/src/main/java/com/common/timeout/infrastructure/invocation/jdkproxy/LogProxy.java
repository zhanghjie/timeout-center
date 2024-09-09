package com.common.timeout.infrastructure.invocation.jdkproxy;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Proxy
 * 功能描述：TODO
 *
 * @author zhanghaojie
 * @date 2022/9/30 11:43
 */
@Slf4j
public class LogProxy implements InvocationHandler {

    private Object targetObject;

    LogProxy(Object target) {
        this.targetObject = target;
    }

    /**
     * 代理方法逻辑
     *
     * @param proxy  代理对象
     * @param method 当前调度方法
     * @param args   当前方法参数
     * @return 代理结果返回
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        handle();
        System.out.println("拦截方法：" + method);
        System.out.println("方法参数：" + Arrays.toString(args));
        Object obj = method.invoke(targetObject, args);
        return obj;
    }


    private void handle() {
        //处理一些业务逻辑
        System.out.println("JDKProxy处理业务逻辑");
    }
}