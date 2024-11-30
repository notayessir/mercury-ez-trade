package com.notayessir.quote.api.spot.mq;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;


@Getter
@Setter
public class DepthDTO implements Serializable {

    private Long coinId;

    private String code;

    private BigDecimal precision;

    // sell
    private Map<BigDecimal, BigDecimal> ask;

    // buy
    private Map<BigDecimal, BigDecimal> bid;

}
