package com.common.timeout.infrastructure.invocation.jdkproxy;

import java.lang.reflect.Proxy;

/**
 * LocalProxyFactory
 * 功能描述：本地代理工厂
 *
 * @author zhanghaojie
 * @date 2022/10/24 15:17
 */
public class LocalProxyFactory {
    @SuppressWarnings("unchecked")
    public static <T> T getProxy(T target) {
        return (T) Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                new LogProxy(target));
    }


}
