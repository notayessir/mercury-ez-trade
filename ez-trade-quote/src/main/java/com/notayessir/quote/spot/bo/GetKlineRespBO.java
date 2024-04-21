package com.notayessir.quote.spot.bo;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class GetKlineRespBO {

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
