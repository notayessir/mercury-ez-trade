package com.notayessir.quote.api.spot.mq;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;


@Getter
@Setter
public class QKLineDTO implements Serializable {


    private Long coinId;

    private String timeInterval;

    private Long statisticStartTime;

    private String openPrice;

    private String highPrice;

    private String lowPrice;

    private String closePrice;

    private String clinchQty;

    private String clinchAmount;

    private Integer version;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}
