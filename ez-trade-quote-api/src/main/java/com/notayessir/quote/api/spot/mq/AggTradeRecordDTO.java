package com.notayessir.quote.api.spot.mq;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;


@Getter
@Setter
public class AggTradeRecordDTO implements Serializable {

    private Long coinId;

    private String code;

    private String clinchQty;

    private String clinchAmount;

    private Long tickTimestamp;

    private Long txSequence;

    private Long globalSequence;

    private LocalDateTime createTime;
}
