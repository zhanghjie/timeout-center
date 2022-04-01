package com.common.timeout.api.enums;

/**
 * TimeoutCenterStateEnum
 * 功能描述: 超时中心任务状态枚举类
 *
 * @author zhanghaojie
 * @date 2021/12/13 18:42
 */
public enum TimeoutCenterStateEnum {
    /**
     * 待处理
     */
    WAIT(0, "待处理"),
    /**
     * 已处理
     */
    SUCCESS(1, "已处理"),
    /**
     * 取消
     */
    CANCEL(2, "取消"),
    ;


    private Integer code;
    private String name;

    TimeoutCenterStateEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static TimeoutCenterStateEnum getByCode(String code) {
        for (TimeoutCenterStateEnum enumVo : TimeoutCenterStateEnum.values()) {
            if (enumVo.getCode().equals(code)) {
                return enumVo;
            }
        }
        return null;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }


}
