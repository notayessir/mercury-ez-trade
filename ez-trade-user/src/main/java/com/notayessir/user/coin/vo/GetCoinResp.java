package com.notayessir.user.coin.vo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class GetCoinResp {

    private Long id;

    private String baseCurrency;

    private String baseMinQty;

    private String baseMaxQty;

    private Integer baseScale;

    private String quoteCurrency;

    private String quoteMinQty;

    private String quoteMaxQty;

    private Integer quoteScale;

}
