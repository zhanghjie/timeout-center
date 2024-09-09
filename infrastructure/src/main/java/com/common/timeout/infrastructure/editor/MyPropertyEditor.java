package com.common.timeout.infrastructure.editor;

import java.beans.PropertyEditorSupport;

/**
 * MyPropertyEditor
 * 功能描述：自定义解析类
 *
 * @author zhanghaojie
 * @date 2023/1/9 18:17
 */
public class MyPropertyEditor extends PropertyEditorSupport {
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        super.setAsText(text);
    }
}
