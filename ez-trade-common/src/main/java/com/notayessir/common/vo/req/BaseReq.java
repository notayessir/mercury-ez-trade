package com.notayessir.common.vo.req;


import com.notayessir.common.constant.EnumRequestSource;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseReq {

    protected Long userId;

    protected String ip;

    protected String requestId;

    protected EnumRequestSource requestSource;

}
