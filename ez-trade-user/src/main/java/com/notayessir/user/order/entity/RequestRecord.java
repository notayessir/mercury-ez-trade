package com.notayessir.user.order.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@TableName("t_request_record")
public class RequestRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String requestId;

    private String requestTarget;

    private Long businessId;

    private LocalDateTime createTime;
}
