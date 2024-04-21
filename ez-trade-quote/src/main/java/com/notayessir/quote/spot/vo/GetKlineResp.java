package com.notayessir.quote.spot.vo;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetKlineResp {

    private Long id;

    private Long coinId;

    private String timeInterval;

    private Long statisticStartTime;

    private String openPrice;

    private String highPrice;

    private String lowPrice;

    private String closePrice;

    private String clinchQty;

    private String clinchAmount;

}
