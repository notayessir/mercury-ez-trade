package com.notayessir.user.order.vo;


import com.notayessir.common.vo.ReqCheck;
import com.notayessir.common.vo.req.BaseReq;
import com.notayessir.common.web.BusinessException;
import com.notayessir.user.common.constant.EnumUserResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class FindOrderReq extends BaseReq implements ReqCheck {

    private Long orderId;
    @Override
    public void checkAndInit() {
        if (Objects.isNull(orderId)){
            throw new BusinessException(EnumUserResponse.PARAM_BLANK.getCode(), "field orderId is blank");
        }
        if (Objects.isNull(userId)){
            throw new BusinessException(EnumUserResponse.PARAM_BLANK.getCode(), "field userId is blank");
        }
    }
}
