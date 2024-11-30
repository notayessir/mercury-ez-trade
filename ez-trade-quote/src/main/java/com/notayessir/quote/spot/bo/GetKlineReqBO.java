package com.notayessir.quote.spot.bo;


import com.notayessir.common.vo.req.BaseReq;
import com.notayessir.quote.spot.constant.EnumKLineInterval;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetKlineReqBO extends BaseReq {


    private Long coinId;
    private String symbol;

    private String interval = EnumKLineInterval.ONE_HOUR.name();

    private Long startTime;
    private Long endTime;


}
