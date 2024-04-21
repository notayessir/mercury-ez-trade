package com.notayessir.user.order.constant;


import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
public enum EnumOrderPatchAction {

    CANCEL("CANCEL"),




    ;


    private final String action;

    EnumOrderPatchAction(String action) {
        this.action = action;
    }


    public static EnumOrderPatchAction getByAction(String action){
        EnumOrderPatchAction[] values = EnumOrderPatchAction.values();
        for (EnumOrderPatchAction value : values) {
            if (StringUtils.equalsIgnoreCase(value.action ,action))
                return value;
        }
        return null;
    }

}
