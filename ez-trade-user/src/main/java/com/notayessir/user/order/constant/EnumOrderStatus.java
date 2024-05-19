package com.notayessir.user.order.constant;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public enum EnumOrderStatus {

    CANCELLED(-20, "订单已取消", false),

    CANCELLING(-10, "订单取消中", false),

    CREATE(10, "订单已创建", true),

    SUBMITTED(15, "订单已提交", false),

    TO_FILL(20, "订单待成交", true),

    PARTIAL_FILLED(30, "订单部分成交", true),

    PARTIAL_FILLED_AND_CANCEL(40, "订单成交已取消", false),

    FILLED(50, "订单已成交", false),


    ;


    private static final List<Integer> ENDED_STATUS = Arrays.asList(
            CANCELLED.code,
            FILLED.code,
            PARTIAL_FILLED_AND_CANCEL.code
    );

    private static final List<Integer> PROCESSING_STATUS = Arrays.asList(
            TO_FILL.code,
            PARTIAL_FILLED.code
    );


    private final int code;

    private final String desc;

    private final boolean cancelable;

    EnumOrderStatus(Integer code, String desc, boolean cancelable) {
        this.code = code;
        this.desc = desc;
        this.cancelable = cancelable;
    }

    public static EnumOrderStatus getEnumOrderStatus(int code){
        EnumOrderStatus[] values = EnumOrderStatus.values();
        for (EnumOrderStatus value : values) {
            if (value.code == code){
                return value;
            }
        }
        return null;
    }

    public static boolean isEndedStatus(Integer code){

        return ENDED_STATUS.contains(code);
    }

    public static boolean isProcessingStatus(Integer code){

        return PROCESSING_STATUS.contains(code);
    }

}
