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
 * @since 2024-04-14
 */
@Getter
@Setter
@TableName("t_handicap")
public class Handicap implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long coinId;

    private String price;

    private String qty;

    private Long version;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Integer entrustSide;
}
