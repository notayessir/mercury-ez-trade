package com.notayessir.user.user.vo;


import com.notayessir.common.vo.ReqCheck;
import com.notayessir.common.vo.req.BaseReq;
import com.notayessir.common.web.BusinessException;
import com.notayessir.user.common.constant.EnumUserResponse;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
public class DepositReq extends BaseReq implements ReqCheck {


    private String currency;

    private BigDecimal available;

    @Override
    public void checkAndInit() {
        if (Objects.isNull(available) || available.compareTo(BigDecimal.ZERO) <= 0){
            throw new BusinessException(EnumUserResponse.PARAM_BLANK.getCode(), EnumUserResponse.PARAM_BLANK.getMessage());

        }
        if (StringUtils.isBlank(currency)){
            throw new BusinessException(EnumUserResponse.PARAM_BLANK.getCode(), EnumUserResponse.PARAM_BLANK.getMessage());

        }
    }
}
