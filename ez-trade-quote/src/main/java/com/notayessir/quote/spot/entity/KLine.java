package com.notayessir.quote.spot.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("t_k_line")
public class KLine implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long coinId;

    private String timeInterval;

    private Long statisticStartTime;

    private LocalDateTime statisticStartTimeHint;

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
