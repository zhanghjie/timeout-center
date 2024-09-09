package com.common.timeout.infrastructure.invocation;

import com.common.timeout.infrastructure.annotation.ToLog;
import com.common.timeout.infrastructure.impl.TaskTypeMangerServiceImpl;
import com.common.timeout.infrastructure.invocation.cglib.TimeoutCenterLogMethodInterceptor;
import com.common.timeout.infrastructure.invocation.jdkproxy.LocalProxyFactory;
import org.ehcache.core.util.CollectionUtil;
import org.junit.platform.commons.util.AnnotationUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.InjectionMetadata;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.*;
import java.util.function.Function;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

/**
 * TimeOutRegistryPostProcessor
 * 功能描述：TODO
 *
 * @author zhanghaojie
 * @date 2022/10/21 14:26
 */
@Component
public class TimeOutRegistryPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean.getClass().isInterface()) {
            // 获取 代理类
            return LocalProxyFactory.getProxy(bean);
        }
        return bean;
    }

}
