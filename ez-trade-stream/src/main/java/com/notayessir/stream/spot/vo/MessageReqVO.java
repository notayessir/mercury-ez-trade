package com.notayessir.stream.spot.vo;


import lombok.Getter;
import lombok.Setter;
import org.springframework.web.socket.WebSocketSession;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class MessageReqVO implements Serializable {

    private Integer id;
    private String method;
    private List<String> params;
    private transient WebSocketSession session;




}
