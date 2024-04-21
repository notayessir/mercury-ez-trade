package com.notayessir.user.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("t_user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String email;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private String username;

    private String phone;

    private String salt;

    private String password;

    private Integer version;

    private String secret;

    private Integer deleted;
}
