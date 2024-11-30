package com.notayessir.stream.spot.vo;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class MessageRespVO implements Serializable {

    private Integer id;

    public MessageRespVO(Integer id) {
        this.id = id;
    }
}
