package com.notayessir.queue.common.constant;

import lombok.Getter;

@Getter
public enum EnumResponseOfQueue {

    SUCCESS("e00000", "success"),

    ERROR("e00001", "error"),

    PARAM_BLANK("e00002", "param.blank"),


    PARAM_INVALID("e00003", "param.invalid"),


    WRONG_PASSWORD("e00006", "wrong.password"),

    AUTHORIZATION_REQUIRE("e00007", "token.require"),


    AUTHORIZATION_EXPIRED("e00008", "token.expired"),

    FAIL_TO_SEND_MQ_MESSAGE("e00009", "fail.to.send.mq.message"),
    PRODUCT_NOT_EXIST("e00010", "product.not.exist"),

    ACCOUNT_NOT_EXIST("e00011", "account.not.exist"),

    FAIL_TO_DEPOSIT("e00012", "fail.to.deposit"),

    FAIL_TO_FREEZE("e00013", "fail.to.freeze"),


    AVAILABLE_INSUFFICIENT("e00014", "available.insufficient"),



    HOLD_INSUFFICIENT("e00016", "hold.insufficient"),


    ORDER_COMPLETED("e00015", "order.completed"),

    ORDER_CANCELLING("e00017", "order.cancelling"),

    ORDER_CANCELLED("e00018", "order.cancelled"),

    ORDER_CREATED("e00019", "order.created"),


    VERIFY_CODE_INVALID("e00020", "verify.code.invalid"),

    ORDER_FAIL_TO_CANCEL("e00021", "order.fail.to.cancel"),

    VERSION_OUTDATED("e00022", "version.outdated"),

    ROUTE_NOT_EXIST("e00023", "route.not.exist"),


    FAIL_TO_ROUTE("e00024", "fail.to.route"),

    ;

    EnumResponseOfQueue(String code, String message) {
        this.code = code;
        this.message = message;
    }

    private final String code;

    private final String message;

}
