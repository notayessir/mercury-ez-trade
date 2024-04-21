package com.notayessir.user.coin.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("t_coin_pair")
public class CoinPair implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Integer deleted;

    private Integer version;

    private String baseCurrency;

    private String baseMinQty;

    private String baseMaxQty;

    private Integer baseScale;

    private String quoteCurrency;

    private String quoteMinQty;

    private String quoteMaxQty;

    private Integer quoteScale;
}
