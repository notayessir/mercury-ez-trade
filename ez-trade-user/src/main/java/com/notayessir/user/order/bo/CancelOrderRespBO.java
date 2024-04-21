package com.notayessir.user.order.bo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CancelOrderRespBO {

    private Long orderId;

}
