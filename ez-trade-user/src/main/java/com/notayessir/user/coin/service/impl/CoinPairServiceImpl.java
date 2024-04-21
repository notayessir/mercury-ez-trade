package com.notayessir.user.coin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.notayessir.common.vo.req.BasePageReq;
import com.notayessir.common.web.BusinessException;
import com.notayessir.user.api.order.constant.EnumEntrustType;
import com.notayessir.user.coin.bo.CheckCoinResultBO;
import com.notayessir.user.coin.bo.GetCoinReqBO;
import com.notayessir.user.coin.entity.CoinPair;
import com.notayessir.user.coin.mapper.CoinPairMapper;
import com.notayessir.user.coin.service.ICoinPairService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.notayessir.user.common.vo.EnumUserResponse;
import com.notayessir.user.order.bo.CreateOrderReqBO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;

@Service
public class CoinPairServiceImpl extends ServiceImpl<CoinPairMapper, CoinPair> implements ICoinPairService {

    @Override
    public void createCoin(CoinPair coinPair) {
        save(coinPair);
    }

    @Override
    public Page<CoinPair> getCoins(BasePageReq<GetCoinReqBO> req) {
        Page<CoinPair> iPage = Page.of(req.getPageNum(), req.getPageSize());
        LambdaQueryWrapper<CoinPair> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .orderByDesc(CoinPair::getId);
        return this.page(iPage, queryWrapper);
    }

    @Override
    public CheckCoinResultBO checkCoin(CreateOrderReqBO reqBO) {
        // checkAndInit product if offline
        CoinPair coinPair = getById(reqBO.getCoinId());
        if (Objects.isNull(coinPair)){
            throw new BusinessException(EnumUserResponse.COIN_NOT_EXIST.getCode(), EnumUserResponse.COIN_NOT_EXIST.getMessage());
        }
        CheckCoinResultBO resultBO = new CheckCoinResultBO();
        resultBO.setCoinPair(coinPair);
        // market order
        if (reqBO.getEntrustType().equals(EnumEntrustType.MARKET.getType())){
            BigDecimal entrustAmount = reqBO.getEntrustAmount();
            // checkAndInit precision
            int scale = entrustAmount.stripTrailingZeros().scale();
            if (scale > coinPair.getBaseScale()){
                throw new BusinessException(EnumUserResponse.COIN_NOT_EXIST.getCode(), EnumUserResponse.COIN_NOT_EXIST.getMessage());
            }
        } else if (reqBO.getEntrustType().equals(EnumEntrustType.NORMAL_LIMIT.getType())) {
            // checkAndInit range and precision of entrust number
            BigDecimal entrustQty = reqBO.getEntrustQty();
//            if (entrustNum.compareTo(new BigDecimal(coinPair.getBaseMinQty())) < 0
//                    || entrustNum.compareTo(new BigDecimal(coinPair.getBaseMaxQty())) > 0){
//                throw new BusinessException(EnumResponseOfUser.COIN_NOT_EXIST.getCode(), EnumResponseOfUser.COIN_NOT_EXIST.getMessage());
//            }
            int scale = entrustQty.stripTrailingZeros().scale();
            if (scale > coinPair.getQuoteScale()){
                throw new BusinessException(EnumUserResponse.COIN_NOT_EXIST.getCode(), EnumUserResponse.COIN_NOT_EXIST.getMessage());
            }

            // checkAndInit range and precision of entrust price
            BigDecimal entrustPrice = reqBO.getEntrustPrice();
//            if (entrustPrice.compareTo(new BigDecimal(coinPair.getQuoteMinQty())) < 0
//                    || entrustPrice.compareTo(new BigDecimal(coinPair.getQuoteMaxQty())) > 0){
//                throw new BusinessException(EnumResponseOfUser.COIN_NOT_EXIST.getCode(), EnumResponseOfUser.COIN_NOT_EXIST.getMessage());
//
//            }
            scale = entrustPrice.stripTrailingZeros().scale();
            if (scale > coinPair.getBaseScale()){
                throw new BusinessException(EnumUserResponse.COIN_NOT_EXIST.getCode(), EnumUserResponse.COIN_NOT_EXIST.getMessage());
            }

            // checkAndInit range and precision of entrust price
//            BigDecimal entrustAmount = reqBO.getEntrustAmount();
//            if (entrustAmount.compareTo(new BigDecimal(coinPair.getQuoteMinQty())) < 0
//                    || entrustAmount.compareTo(new BigDecimal(coinPair.getQuoteMaxQty())) > 0){
//                throw new BusinessException(EnumResponseOfUser.COIN_NOT_EXIST.getCode(), EnumResponseOfUser.COIN_NOT_EXIST.getMessage());
//            }
//            scale = entrustAmount.stripTrailingZeros().scale();
//            if (scale > coinPair.getQuoteScale()){
//                throw new BusinessException(EnumResponseOfUser.COIN_NOT_EXIST.getCode(), EnumResponseOfUser.COIN_NOT_EXIST.getMessage());
//
//            }


        }else if (reqBO.getEntrustType().equals(EnumEntrustType.PREMIUM_LIMIT.getType())) {
            // checkAndInit range and precision of entrust number
            BigDecimal entrustQty = reqBO.getEntrustQty();
//            if (entrustNum.compareTo(new BigDecimal(coinPair.getBaseMinQty())) < 0
//                    || entrustNum.compareTo(new BigDecimal(coinPair.getBaseMaxQty())) > 0){
//                throw new BusinessException(EnumResponseOfUser.COIN_NOT_EXIST.getCode(), EnumResponseOfUser.COIN_NOT_EXIST.getMessage());
//
//            }
            int scale = entrustQty.stripTrailingZeros().scale();
            if (scale > coinPair.getQuoteScale()){
                throw new BusinessException(EnumUserResponse.COIN_NOT_EXIST.getCode(), EnumUserResponse.COIN_NOT_EXIST.getMessage());

            }

            // checkAndInit range and precision of entrust price
            BigDecimal entrustPrice = reqBO.getEntrustPrice();
//            if (entrustPrice.compareTo(new BigDecimal(coinPair.getQuoteMinQty())) < 0
//                    || entrustPrice.compareTo(new BigDecimal(coinPair.getQuoteMaxQty())) > 0){
//                throw new BusinessException(EnumResponseOfUser.COIN_NOT_EXIST.getCode(), EnumResponseOfUser.COIN_NOT_EXIST.getMessage());
//
//            }

            scale = entrustPrice.stripTrailingZeros().scale();
            if (scale > coinPair.getBaseScale()){
                throw new BusinessException(EnumUserResponse.COIN_NOT_EXIST.getCode(), EnumUserResponse.COIN_NOT_EXIST.getMessage());

            }

            // checkAndInit range and precision of trigger price
//            BigDecimal triggerPrice = reqBO.getTriggerPrice();
//            if (triggerPrice.compareTo(new BigDecimal(coinPair.getQuoteMinQty())) < 0
//                    || triggerPrice.compareTo(new BigDecimal(coinPair.getQuoteMaxQty())) > 0){
//                throw new BusinessException(EnumResponseOfUser.COIN_NOT_EXIST.getCode(), EnumResponseOfUser.COIN_NOT_EXIST.getMessage());
//
//            }
//            scale = triggerPrice.stripTrailingZeros().scale();
//            if (scale > coinPair.getQuoteScale()){
//                throw new BusinessException(EnumResponseOfUser.COIN_NOT_EXIST.getCode(), EnumResponseOfUser.COIN_NOT_EXIST.getMessage());
//            }

            // checkAndInit range and precision of entrust price
//            BigDecimal entrustAmount = reqBO.getEntrustAmount();
//            if (entrustTotalPrice.compareTo(new BigDecimal(coinPair.getQuoteMinQty())) < 0
//                    || entrustTotalPrice.compareTo(new BigDecimal(coinPair.getQuoteMaxQty())) > 0){
//                throw new BusinessException(EnumResponseOfUser.COIN_NOT_EXIST.getCode(), EnumResponseOfUser.COIN_NOT_EXIST.getMessage());
//
//            }

//            scale = entrustAmount.stripTrailingZeros().scale();
//            if (scale > coinPair.getQuoteScale()){
//                throw new BusinessException(EnumResponseOfUser.COIN_NOT_EXIST.getCode(), EnumResponseOfUser.COIN_NOT_EXIST.getMessage());
//            }
        }

        return resultBO;
    }
}
