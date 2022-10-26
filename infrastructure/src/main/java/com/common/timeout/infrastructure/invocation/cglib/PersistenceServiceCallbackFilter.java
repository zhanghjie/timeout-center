package com.common.timeout.infrastructure.invocation.cglib;

import net.sf.cglib.proxy.CallbackFilter;
import org.apache.commons.collections4.SetUtils;

import java.lang.reflect.Method;
import java.util.Set;

/**
 * PersistenceServiceCallbackFilter
 * 功能描述：TODO
 *
 * @author zhanghaojie
 * @date 2022/10/25 15:03
 */
public class PersistenceServiceCallbackFilter implements CallbackFilter {

    /**
     * 需要回调的方法在数组中的下标
     */
    public static final int CALLBACK = 0;

    /**
     * 忽略的方法
     */
    public static final int DONOTING = 1;

    private static final Set<String> IGNORE_METHOD_SET = SetUtils.hashSet("toString", "hashCode", "toString");

    /**
     * 指定哪个callback被调用当这个方法被调用的时候，callback
     * 就是一个MethodInterceptor
     *
     * @param method 正在被调用的方法
     * @return 这个方法在callback数组中的下标
     */
    @Override
    public int accept(Method method) {
        String methodName = method.getName();
        if (IGNORE_METHOD_SET.contains(methodName)) {
            return DONOTING;
        }
        return CALLBACK;
    }
}
