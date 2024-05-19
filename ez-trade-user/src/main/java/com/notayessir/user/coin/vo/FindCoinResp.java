package com.notayessir.user.coin.vo;

import com.notayessir.user.coin.bo.FindCoinRespBO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FindCoinResp {

    private Long id;

    private String baseCurrency;

    private String baseMinQty;

    private String baseMaxQty;

    private Integer baseScale;

    private String quoteCurrency;

    private String quoteMinQty;

    private String quoteMaxQty;

    private Integer quoteScale;

    public FindCoinResp(FindCoinRespBO record) {
        this.id = record.getId();
        this.baseCurrency = record.getBaseCurrency();
        this.baseMaxQty = record.getBaseMaxQty();
        this.baseMinQty = record.getBaseMinQty();
        this.baseScale = record.getBaseScale();

        this.quoteCurrency = record.getQuoteCurrency();
        this.quoteMaxQty = record.getQuoteMaxQty();
        this.quoteMinQty = record.getQuoteMinQty();
        this.quoteScale = record.getQuoteScale();
    }
}
