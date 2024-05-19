package com.notayessir.quote.spot.bo;


import com.notayessir.common.vo.req.BaseReq;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetKlineReqBO extends BaseReq {



    private Long coinId;

    private String interval;

}
