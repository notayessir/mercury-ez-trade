package com.notayessir.user.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("t_account")
public class Account implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long userId;

    private Integer accountType;

    private String currency;

    private String available;

    private String hold;

    private Integer version;

    private Integer deleted;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
