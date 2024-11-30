package com.notayessir.quote.spot.vo;


import com.notayessir.common.vo.ReqCheck;
import com.notayessir.common.vo.req.BaseReq;
import com.notayessir.common.web.BusinessException;
import com.notayessir.quote.common.constant.EnumQuoteResponse;
import com.notayessir.quote.spot.constant.EnumKLineInterval;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

@Getter
@Setter
public class GetKlineReq extends BaseReq implements ReqCheck {

    private String symbol;

    private String interval = EnumKLineInterval.ONE_HOUR.name();

    private Long startTime;
    private Long endTime;
    private Long coinId;



    @Override
    public void checkAndInit() {
        if (Objects.isNull(coinId)){
            throw new BusinessException(EnumQuoteResponse.PARAM_BLANK.getCode(), EnumQuoteResponse.PARAM_BLANK.getMessage());
        }
        if (StringUtils.isBlank(symbol)){
            throw new BusinessException(EnumQuoteResponse.PARAM_BLANK.getCode(), EnumQuoteResponse.PARAM_BLANK.getMessage());
        }
    }
}
