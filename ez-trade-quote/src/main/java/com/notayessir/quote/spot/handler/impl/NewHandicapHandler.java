package com.notayessir.quote.spot.handler.impl;

import cn.hutool.core.util.IdUtil;
import com.notayessir.bo.MatchResultBO;
import com.notayessir.bo.OrderItemBO;
import com.notayessir.common.constant.EnumFieldVersion;
import com.notayessir.quote.api.spot.mq.QOrderBookDTO;
import com.notayessir.quote.spot.entity.Handicap;
import com.notayessir.quote.spot.handler.HandicapHandler;
import com.notayessir.quote.spot.service.IHandicapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;


@Service
public class NewHandicapHandler implements HandicapHandler {



    @Autowired
    private IHandicapService iHandicapService;


    @Override
    public void handleHandicapUpdatedEvent(MatchResultBO event) {
        // 1. simply add up qty
        OrderItemBO takerOrder = event.getTakerOrder();
        Integer entrustSide = takerOrder.getEntrustSide();
        Long coinId = event.getCoinId();
        BigDecimal entrustQty = takerOrder.getEntrustQty();
        Handicap forwardSideHandicap = iHandicapService.findHandicap(coinId, takerOrder.getEntrustPrice(), entrustSide);
        if (Objects.isNull(forwardSideHandicap)){
            forwardSideHandicap = buildNewHandicap(takerOrder.getEntrustPrice(), entrustQty, coinId, entrustSide);
            iHandicapService.saveHandicapAndSyncCache(forwardSideHandicap);
        } else {
            buildUpdateHandicap(forwardSideHandicap, entrustQty);
            iHandicapService.updateHandicapAndSyncCache(forwardSideHandicap);
        }
    }

    @Override
    public List<QOrderBookDTO> handleHandicapUpdatedEventWithReturn(MatchResultBO event) {
        handleHandicapUpdatedEvent(event);

        return iHandicapService.findHandicapFromCache(event.getCoinId());
    }


    private void buildUpdateHandicap(Handicap handicap, BigDecimal toClinchQty) {
        BigDecimal qty = new BigDecimal(handicap.getQty()).add(toClinchQty);

        handicap.setQty(qty.toString());
        handicap.setUpdateTime(LocalDateTime.now());
    }

    private Handicap buildNewHandicap(BigDecimal price, BigDecimal toClinchQty, Long coinId, Integer entrustSide) {
        Handicap handicap = new Handicap();
        handicap.setQty(toClinchQty.toString());
        handicap.setPrice(price.toString());
        handicap.setEntrustSide(entrustSide);
        handicap.setId(IdUtil.getSnowflakeNextId());
        handicap.setVersion((long) EnumFieldVersion.INIT.getCode());
        handicap.setCoinId(coinId);
        LocalDateTime now = LocalDateTime.now();
        handicap.setUpdateTime(now);
        handicap.setCreateTime(now);
        return handicap;
    }
}
