package com.notayessir.quote.spot.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author nobody
 * @since 2024-11-10
 */
@Getter
@Setter
@TableName("agg_trade_record")
public class AggTradeRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long coinId;

    private String clinchQty;

    private String clinchAmount;

    private Long tickTimestamp;

    private Long txSequence;

    private Long globalSequence;

    private Integer version;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private String event;
}
