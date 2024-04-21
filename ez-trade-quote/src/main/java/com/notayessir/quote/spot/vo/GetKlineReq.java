package com.notayessir.quote.spot.vo;


import com.notayessir.common.vo.ReqCheck;
import com.notayessir.common.vo.req.BaseReq;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetKlineReq extends BaseReq implements ReqCheck {


    private String symbol;

    private String interval;

    @Override
    public void checkAndInit() {

    }
}
