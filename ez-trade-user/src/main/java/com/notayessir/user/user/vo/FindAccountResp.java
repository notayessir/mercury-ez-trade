package com.notayessir.user.user.vo;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FindAccountResp {

    private Long userId;

    private String currency;

    private String available;

    private String hold;

}
