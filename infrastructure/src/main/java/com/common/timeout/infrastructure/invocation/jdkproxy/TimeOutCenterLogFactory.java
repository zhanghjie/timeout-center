package com.common.timeout.infrastructure.invocation.jdkproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * TimeOutCenterBeanFactory
 * 功能描述：TODO
 *
 * @author zhanghaojie
 * @date 2022/10/21 14:42
 */
public class TimeOutCenterLogFactory {
    //维护一个目标对象
    private Object target;

    public TimeOutCenterLogFactory(Object target){
        this.target = target;
    }

    //给目标对象生成代理对象
    public Object getProxyInstance(){
        return Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                new InvocationHandler(){
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable{
                        System.out.println("开始事务");
                        //执行目标对象方法
                        Object returnValue = method.invoke(target ,args);
                        System.out.println("提交事务");
                        return returnValue;
                    }
                }
        );
    }

}
