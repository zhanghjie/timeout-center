package com.common.timeout.client.enums;

/**
 * TimeoutCenterStateEnum
 * 功能描述: 超时中心任务错误码枚举类
 *
 * @author zhanghaojie
 * @date 2021/12/13 18:42
 */
public enum TimeoutCenterCodeEnum {
    /**
     * 成功
     */
    SUCCESS("000000", "成功"),

    /**
     * 查询任务为空
     */
    TASK_IS_EMPTY("200001", "TASK_IS_EMPTY"),
    /**
     * 任务已执行
     */
    TASK_IS_SUCCESS("200002", "TASK_IS_SUCCESS"),
    /**
     * 取消任务失败
     */
    CANCEL_TASK_FAIL("200003", "CANCEL_TASK_FAIL"),

    ;


    private String code;
    private String message;

    TimeoutCenterCodeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static TimeoutCenterCodeEnum getByCode(String code) {
        for (TimeoutCenterCodeEnum enumVo : TimeoutCenterCodeEnum.values()) {
            if (enumVo.getCode().equals(code)) {
                return enumVo;
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }


}
