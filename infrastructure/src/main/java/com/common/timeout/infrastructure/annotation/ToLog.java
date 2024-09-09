package com.common.timeout.infrastructure.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * ToLog
 * 功能描述：接口li打印日志注解
 *
 * @author zhanghaojie
 * @date 2022/10/24 14:28
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ToLog {
    String value() default "";
}
