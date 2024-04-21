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
 * @since 2024-04-01
 */
@Getter
@Setter
@TableName("t_route_info")
public class RouteInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long coinId;

    private String routeTo;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Integer version;

    private Integer deleted;
}
