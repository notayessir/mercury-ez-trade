package com.notayessir.user.order.bo;


import com.notayessir.common.vo.req.BaseReq;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CancelOrderReqBO extends BaseReq {

    private Long orderId;

}
