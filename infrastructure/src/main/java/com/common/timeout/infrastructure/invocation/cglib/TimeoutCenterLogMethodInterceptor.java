package com.common.timeout.infrastructure.invocation.cglib;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.proxy.*;

import java.lang.reflect.Method;

/**
 * TimeoutCenterLogMethodInterceptor
 * 功能描述：CGLIB 代理实现
 *
 * @author zhanghaojie
 * @date 2022/10/24 14:36
 */
@Slf4j
public class TimeoutCenterLogMethodInterceptor implements MethodInterceptor {

    /**
     * 目标对象（也被称为被代理对象）
     */
    private Class target;

    public TimeoutCenterLogMethodInterceptor(Class target) {
        this.target = target;
    }

    /**
     * @param o
     * @param method
     * @param objects
     * @param methodProxy
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        Object object = null;
        try {
            log.info("执行接口拦截before,class:{}, method:{},param:{}", target.getName(), method.getName(), objects);
            object = methodProxy.invokeSuper(o, objects);
            log.info("执行接口拦截after,method:{},result:{}", method.getName(), JSON.toJSONString(object));
        } catch (Exception e) {
            log.error("代理拦截发生异常,{}", e);
        }
        return object;
    }

    /**
     * 获取被代理接口实例对象
     * <p>
     * 通过 enhancer.create 可以获得一个代理对象，它继承了 target.getClass() 类
     *
     * @param <T>
     * @return
     */
    public <T> T getProxy() {
        Enhancer enhancer = new Enhancer();
        //设置被代理类
        enhancer.setSuperclass(target);
        // 设置回调
        enhancer.setCallbacks(new Callback[]{this, NoOp.INSTANCE});
        enhancer.setCallbackFilter(new PersistenceServiceCallbackFilter());
        // create方法正式创建代理类
        return (T) enhancer.create();
    }
}
