package com.notayessir.queue.route.entity;

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
 * @since 2024-01-18
 */
@Getter
@Setter
@TableName("t_request_record")
public class RequestRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long requestId;

    private Integer commandType;

    private Long orderId;

    private LocalDateTime createTime;

    private Integer opStatus;

    private LocalDateTime updateTime;

    private Integer version;

    private String eventContent;
}
