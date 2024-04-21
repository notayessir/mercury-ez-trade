package com.notayessir.user.coin.bo;


import com.notayessir.common.constant.EnumRequestSource;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CreateCoinReqBO {


    private EnumRequestSource requestSource;
    private String baseCurrency;

    private String quoteCurrency;

    private BigDecimal baseMinQty;

    private BigDecimal baseMaxQty;

    private Integer baseScale;

    private BigDecimal quoteMinQty;

    private BigDecimal quoteMaxQty;

    private Integer quoteScale;


}
