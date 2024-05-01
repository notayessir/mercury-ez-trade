package com.notayessir.user.user.bo;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FindAccountRespBO {

    private Long userId;

    private String currency;

    private String available;

    private String hold;
}
