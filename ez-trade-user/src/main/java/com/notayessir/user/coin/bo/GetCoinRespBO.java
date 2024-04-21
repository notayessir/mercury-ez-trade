package com.notayessir.user.coin.bo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class GetCoinRespBO {

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
