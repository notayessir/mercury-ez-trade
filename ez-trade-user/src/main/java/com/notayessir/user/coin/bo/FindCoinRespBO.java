package com.notayessir.user.coin.bo;

import com.notayessir.user.coin.entity.CoinPair;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FindCoinRespBO {

    private Long id;

    private String baseCurrency;

    private String baseMinQty;

    private String baseMaxQty;

    private Integer baseScale;

    private String quoteCurrency;

    private String quoteMinQty;

    private String quoteMaxQty;

    private Integer quoteScale;

    public FindCoinRespBO() {
    }

    public FindCoinRespBO(CoinPair record) {
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
