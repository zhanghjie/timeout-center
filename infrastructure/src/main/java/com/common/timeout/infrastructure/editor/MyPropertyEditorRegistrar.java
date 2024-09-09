package com.common.timeout.infrastructure.editor;

import com.common.timeout.infrastructure.timewheel.vo.TimerTask;
import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.validation.DataBinder;

import java.beans.PropertyEditor;

/**
 * MyPropertyEditorRegistrar
 * 功能描述：自定义editor注册类
 *
 * @author zhanghaojie
 * @date 2023/1/9 18:15
 */
public class MyPropertyEditorRegistrar implements PropertyEditorRegistrar {
    /**
     * Register custom {@link PropertyEditor PropertyEditors} with
     * the given {@code PropertyEditorRegistry}.
     * <p>The passed-in registry will usually be a {@link BeanWrapper} or a
     * {@link DataBinder DataBinder}.
     * <p>It is expected that implementations will create brand new
     * {@code PropertyEditors} instances for each invocation of this
     * method (since {@code PropertyEditors} are not threadsafe).
     *
     * @param registry the {@code PropertyEditorRegistry} to register the
     *                 custom {@code PropertyEditors} with
     */
    @Override
    public void registerCustomEditors(PropertyEditorRegistry registry) {
        registry.registerCustomEditor(TimerTask.class, new MyPropertyEditor());
    }
}
