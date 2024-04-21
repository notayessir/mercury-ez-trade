package com.notayessir.user.user.entity;

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
 * @since 2024-03-29
 */
@Getter
@Setter
@TableName("t_capital_record")
public class CapitalRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long userId;

    private String currency;

    private String num;

    private Integer version;

    private Integer deleted;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Integer businessCode;

    private Integer direction;
}
