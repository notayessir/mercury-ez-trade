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
            throw new BusinessException(EnumUserResponse.PARAM_BLANK.getCode(), EnumUserResponse.PARAM_BLANK.getMessage());
        }
        EnumEntrustSide entrustSide = EnumEntrustSide.getEntrustSide(side);
        if (Objects.isNull(entrustSide)){
            throw new BusinessException(EnumUserResponse.PARAM_BLANK.getCode(), EnumUserResponse.PARAM_BLANK.getMessage());
        }
//        if (Objects.isNull(basePrice)){
//            throw new BusinessException(EnumResponseOfUser.PARAM_BLANK.getCode(), EnumResponseOfUser.PARAM_BLANK.getMessage());
//        }

        if (entrustType == EnumEntrustType.MARKET.getType()){
            checkMarketOrder();
            entrustQty = BigDecimal.ZERO;
            entrustPrice = BigDecimal.ZERO;
        } else if (entrustType == EnumEntrustType.NORMAL_LIMIT.getType() || entrustType == EnumEntrustType.PREMIUM_LIMIT.getType()){
            checkLimitOrder();
            if (entrustType == EnumEntrustType.PREMIUM_LIMIT.getType()){
                EnumEntrustProp prop = EnumEntrustProp.getByType(entrustProp);
                if (Objects.isNull(prop)){
                    throw new BusinessException(EnumUserResponse.PARAM_BLANK.getCode(), EnumUserResponse.PARAM_BLANK.getMessage());
                }
            }

            // assign the entrustAmount
            entrustAmount = entrustPrice.multiply(entrustQty);

        } else {
            throw new BusinessException(EnumUserResponse.PARAM_BLANK.getCode(), EnumUserResponse.PARAM_BLANK.getMessage());

        }

    }

    private void checkLimitOrder() {
        if (Objects.isNull(entrustQty) || entrustQty.compareTo(BigDecimal.ZERO) <= 0){
            throw new BusinessException(EnumUserResponse.PARAM_BLANK.getCode(), EnumUserResponse.PARAM_BLANK.getMessage());

        }
        if (Objects.isNull(entrustPrice) || entrustPrice.compareTo(BigDecimal.ZERO) <= 0){
            throw new BusinessException(EnumUserResponse.PARAM_BLANK.getCode(), EnumUserResponse.PARAM_BLANK.getMessage());

        }
    }

    private void checkMarketOrder() {
        if (Objects.isNull(entrustAmount) || entrustAmount.compareTo(BigDecimal.ZERO) <= 0){
            throw new BusinessException(EnumUserResponse.PARAM_BLANK.getCode(), EnumUserResponse.PARAM_BLANK.getMessage());
        }
    }


}
