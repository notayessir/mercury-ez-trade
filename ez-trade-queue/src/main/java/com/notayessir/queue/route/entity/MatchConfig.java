package com.notayessir.queue.route.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("t_match_config")
public class MatchConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private String groupId;

    private String groupAddress;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Integer version;

    private Integer deleted;

    private Integer enable;
}
