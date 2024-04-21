package com.notayessir.user.order.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class ClinchRecordVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long orderId;

    private Long userId;

    private String qty;

    private String price;

    private Long coinId;

    private String role;

    private Long txSequence;

    private Long globalSequence;

    private Integer version;

    private LocalDate tradeDate;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
