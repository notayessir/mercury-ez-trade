package com.notayessir.quote.api.spot.mq;

import com.notayessir.quote.api.spot.constant.EnumPricePrecision;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;


@Getter
@Setter
public class QOrderBookDTO implements Serializable {

    private Long coinId;

    private EnumPricePrecision precision;

    // sell
    private Map<BigDecimal, BigDecimal> ask;
    // buy
    private Map<BigDecimal, BigDecimal> bid;

}
