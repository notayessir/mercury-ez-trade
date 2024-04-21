package com.notayessir.user.user.bo;


import com.notayessir.common.vo.req.BaseReq;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class DepositReqBO extends BaseReq {


    private String currency;

    private BigDecimal available;


}
