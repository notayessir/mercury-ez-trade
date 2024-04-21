package com.notayessir.user.order.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("t_mq_record")
public class MqRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * order id
     */
    private Long orderId;

    private Integer opType;

    private Integer opStatus;

    private Integer version;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
