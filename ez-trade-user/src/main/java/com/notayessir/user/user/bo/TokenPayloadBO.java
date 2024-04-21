package com.notayessir.user.user.bo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class TokenPayloadBO {

    private Long sub;

    private String email;

    private Long iat;

    private Long exp;


}
