package com.notayessir.user.order.vo;

import com.notayessir.common.vo.ReqCheck;
import com.notayessir.common.vo.req.BaseReq;
import com.notayessir.common.web.BusinessException;
import com.notayessir.user.api.order.constant.EnumEntrustProp;
import com.notayessir.user.api.order.constant.EnumEntrustSide;
import com.notayessir.user.api.order.constant.EnumEntrustType;
import com.notayessir.user.common.constant.EnumUserResponse;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Objects;


@Getter
@Setter
public class CreateOrderReq extends BaseReq implements ReqCheck {



    /**
     * coin id
     */
    private Long coinId;


    /**
     * buy or sell
     */
    private String side;

    /**
     * coin price at the moment
     */
    private BigDecimal basePrice;

    /**
     * number of purchase
     */
    private BigDecimal entrustQty;

    /**
     * entrust price
     */
    private BigDecimal entrustPrice;

//    /**
//     * trigger price
//     */
//    private BigDecimal triggerPrice;

    /**
     * entrust amount
     */
    private BigDecimal entrustAmount;



    private Integer entrustProp;

    private Integer entrustType;


    @Override
    public void checkAndInit() {
        if (Objects.isNull(coinId)){
            throw new BusinessException(EnumUserResponse.PARAM_BLANK.getCode(), "field coinId is blank");
        }
        EnumEntrustSide entrustSide = EnumEntrustSide.getEntrustSide(side);
        if (Objects.isNull(entrustSide)){
            throw new BusinessException(EnumUserResponse.PARAM_BLANK.getCode(), "field side is blank");
        }
//        if (Objects.isNull(basePrice)){
//            throw new BusinessException(EnumResponseOfUser.PARAM_BLANK.getCode(), EnumResponseOfUser.PARAM_BLANK.getMessage());
//        }

        if (entrustType == EnumEntrustType.MARKET.getType()){
            initMarketOrderParam();
            entrustPrice = BigDecimal.ZERO;
        } else if (entrustType == EnumEntrustType.NORMAL_LIMIT.getType() || entrustType == EnumEntrustType.PREMIUM_LIMIT.getType()){
            initLimitOrderParam();

            if (entrustType == EnumEntrustType.PREMIUM_LIMIT.getType()){
                EnumEntrustProp prop = EnumEntrustProp.getByType(entrustProp);
                if (Objects.isNull(prop)){
                    throw new BusinessException(EnumUserResponse.PARAM_BLANK.getCode(), "field entrustProp is blank");
                }
            }

        } else {
            throw new BusinessException(EnumUserResponse.PARAM_BLANK.getCode(), "field entrustType is blank");

        }

    }

    private void initLimitOrderParam() {
        if (Objects.isNull(entrustQty) || entrustQty.compareTo(BigDecimal.ZERO) <= 0){
            throw new BusinessException(EnumUserResponse.PARAM_BLANK.getCode(), "field entrustQty is invalid");
        }
        if (Objects.isNull(entrustPrice) || entrustPrice.compareTo(BigDecimal.ZERO) <= 0){
            throw new BusinessException(EnumUserResponse.PARAM_BLANK.getCode(), "field entrustPrice is invalid");
        }
        // assign the entrustAmount
        entrustAmount = entrustPrice.multiply(entrustQty);
    }

    private void initMarketOrderParam() {
        if (StringUtils.equalsIgnoreCase(EnumEntrustSide.SELL.getSide(), side)){
            // SELL
            if (Objects.isNull(entrustQty) || entrustQty.compareTo(BigDecimal.ZERO) <= 0){
                throw new BusinessException(EnumUserResponse.PARAM_BLANK.getCode(), "field entrustQty is invalid");
            }
            entrustAmount = BigDecimal.ZERO;
        } else {
            // BUY
            if (Objects.isNull(entrustAmount) || entrustAmount.compareTo(BigDecimal.ZERO) <= 0){
                throw new BusinessException(EnumUserResponse.PARAM_BLANK.getCode(), "field entrustAmount is invalid");
            }
            entrustQty = BigDecimal.ZERO;
        }

    }


}
