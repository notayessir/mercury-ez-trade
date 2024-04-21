package com.notayessir.user.order.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author nobody
 * @since 2024-02-14
 */
@Getter
@Setter
@TableName("t_clinch_record")
public class ClinchRecord implements Serializable {

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
