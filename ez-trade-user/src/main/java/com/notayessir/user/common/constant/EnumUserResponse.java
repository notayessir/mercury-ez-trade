package com.notayessir.user.common.constant;

import lombok.Getter;

@Getter
public enum EnumUserResponse {

    SUCCESS("e00000", "success"),

    ERROR("e00001", "error"),

    PARAM_BLANK("e00002", "param.blank"),


    PARAM_INVALID("e00003", "param.invalid"),

    USERNAME_EXIST("e00004", "username.exist"),

    WRONG_PASSWORD("e00006", "wrong.password"),

    AUTHORIZATION_REQUIRE("e00007", "token.require"),


    AUTHORIZATION_EXPIRED("e00008", "token.expired"),

    FAIL_TO_SEND_MQ_MESSAGE("e00009", "fail.to.send.mq.message"),

    COIN_NOT_EXIST("e00010", "coin.not.exist"),

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

    UNEXPECTED_SEQUENCE("e00023", "unexpected.sequence"),



    RECORD_NOT_EXIST("e00023", "record.not.exist"),

    ;

    EnumUserResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }

    private final String code;

    private final String message;

}
