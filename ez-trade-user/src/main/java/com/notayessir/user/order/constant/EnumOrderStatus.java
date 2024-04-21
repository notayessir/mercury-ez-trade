package com.notayessir.user.order.constant;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public enum EnumOrderStatus {

    CANCELLED(-20, "order cancelled", false),

    CANCELLING(-10, "order cancelling", false),

    CREATE(10, "order created", true),

    SUBMITTED(15, "submitted", false),

    TO_FILL(20, "order to clinch", true),

    PARTIAL_FILLED(30, "order clinched partially", true),

    PARTIAL_FILLED_AND_CANCEL(40, "order clinched partially and cancelled", false),

    FILLED(50, "order clinched", false),


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
