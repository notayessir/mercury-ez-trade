package com.notayessir.user.order.vo;


import com.notayessir.common.vo.ReqCheck;
import com.notayessir.common.vo.req.BaseReq;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModifyOrderReq extends BaseReq implements ReqCheck {
    @Override
    public void checkAndInit() {

    }
}
