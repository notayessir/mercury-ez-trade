package com.notayessir.user.coin.vo;


import com.notayessir.common.vo.ReqCheck;
import com.notayessir.common.web.BusinessException;
import com.notayessir.user.common.constant.EnumUserResponse;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
public class CreateCoinReq implements ReqCheck {


    private String baseCurrency;
    private BigDecimal baseMinQty;
    private BigDecimal baseMaxQty;
    private Integer baseScale;

    private String quoteCurrency;
    private BigDecimal quoteMinQty;
    private BigDecimal quoteMaxQty;
    private Integer quoteScale;


    @Override
    public void checkAndInit() {
        if (StringUtils.isAnyBlank(baseCurrency, quoteCurrency)){

            throw new BusinessException(EnumUserResponse.PARAM_BLANK.getCode(), EnumUserResponse.PARAM_BLANK.getMessage());
        }
        if (Objects.isNull(baseMinQty)){
            throw new BusinessException(EnumUserResponse.PARAM_INVALID.getCode(), EnumUserResponse.PARAM_INVALID.getMessage());
        }
        if (Objects.isNull(baseMaxQty)){
            throw new BusinessException(EnumUserResponse.PARAM_INVALID.getCode(), EnumUserResponse.PARAM_INVALID.getMessage());

        }
        if (Objects.isNull(baseScale)){
            throw new BusinessException(EnumUserResponse.PARAM_INVALID.getCode(), EnumUserResponse.PARAM_INVALID.getMessage());

        }
        if (Objects.isNull(quoteMinQty)){
            throw new BusinessException(EnumUserResponse.PARAM_INVALID.getCode(), EnumUserResponse.PARAM_INVALID.getMessage());

        }
        if (Objects.isNull(quoteMaxQty)){
            throw new BusinessException(EnumUserResponse.PARAM_INVALID.getCode(), EnumUserResponse.PARAM_INVALID.getMessage());
        }
        if (Objects.isNull(quoteScale)){
            throw new BusinessException(EnumUserResponse.PARAM_INVALID.getCode(), EnumUserResponse.PARAM_INVALID.getMessage());
        }
    }

}
