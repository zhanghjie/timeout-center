package com.common.timeout.api.dto;


import java.io.Serializable;
import java.util.Objects;


/**
 * 功能描述: controller统一返回基类
 *
 * @author zhanghaojie
 * @date 2022/2/22 10:27
 */
public class WebResponse<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String successCode = "000000";

    private String code;

    private String msg;

    private Boolean isSuccess;
    private T data;

    private WebResponse(String code) {
        this.code = code;
        setIsSuccess(code);
    }

    private WebResponse(String code, String msg) {
        this.code = code;
        this.msg = msg;
        setIsSuccess(code);
    }

    private WebResponse(String code, T t) {
        this.code = code;
        setIsSuccess(code);
        this.data = t;
    }

    private WebResponse(String code, String msg, T t) {
        this.code = code;
        this.msg = msg;
        setIsSuccess(code);
        this.data = t;
    }

    public static <T> WebResponse<T> returnSuccess() {
        return new WebResponse(successCode);
    }

    public static <T> WebResponse<T> returnSuccess(T t) {
        return new WebResponse(successCode, t);
    }

    public static <T> WebResponse<T> returnFail(String code, String msg) {
        return new WebResponse(code, msg);
    }

    public static <T> WebResponse<T> returnFail(String code, String msg, T t) {
        return new WebResponse(code, msg, t);
    }


    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    private void setIsSuccess(String code) {
        if (Objects.equals(code, successCode)) {
            this.isSuccess = true;
        } else {
            this.isSuccess = false;
        }
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public Boolean getSuccess() {
        return isSuccess;
    }
}
