package com.notayessir.user.order.entity;

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
 * @since 2024-01-19
 */
@Getter
@Setter
@TableName("t_clinch_sequence")
public class ClinchSequence implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long globalSequence;

    private Long orderId;

    private LocalDateTime createTime;

    private Integer opStatus;

    private LocalDateTime updateTime;

    private Integer version;

    private String eventContent;

    private Long txSequence;
}
