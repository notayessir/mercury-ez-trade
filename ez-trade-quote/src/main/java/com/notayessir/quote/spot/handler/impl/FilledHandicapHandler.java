package com.notayessir.quote.spot.handler.impl;

import cn.hutool.core.util.IdUtil;
import com.notayessir.bo.MatchItemBO;
import com.notayessir.bo.MatchResultBO;
import com.notayessir.bo.OrderItemBO;
import com.notayessir.common.constant.EnumFieldVersion;
import com.notayessir.constant.EnumEntrustSide;
import com.notayessir.constant.EnumEntrustType;
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
public class FilledHandicapHandler implements HandicapHandler {


    @Autowired
    private IHandicapService iHandicapService;


    @Override
    public void handleHandicapUpdatedEvent(MatchResultBO event) {
        OrderItemBO takerOrder = event.getTakerOrder();

        Integer entrustSide = takerOrder.getEntrustSide();
        Long coinId = event.getCoinId();
        // 1. add forward side qty if type is equal to NORMAL_LIMIT
        if (takerOrder.getEntrustType() == EnumEntrustType.NORMAL_LIMIT.getType()){
            BigDecimal remainEntrustQty = takerOrder.getRemainEntrustQty();
            Handicap forwardSideHandicap = iHandicapService.findHandicap(coinId, takerOrder.getEntrustPrice(), entrustSide);
            if (Objects.isNull(forwardSideHandicap)){
                forwardSideHandicap = buildNewHandicap(takerOrder.getEntrustPrice(), remainEntrustQty, coinId, entrustSide);
                iHandicapService.saveHandicapAndSyncCache(forwardSideHandicap);
            } else {
                buildUpdateHandicap(forwardSideHandicap, remainEntrustQty);
                iHandicapService.updateHandicapAndSyncCache(forwardSideHandicap);
            }
        }

        // 2. deduct reverse side qty
        List<MatchItemBO> matchItems = event.getMatchItems();
        for (MatchItemBO matchItem : matchItems) {
            int reversedEntrustSide = EnumEntrustSide.reverseEntrustSide(entrustSide).getCode();
            Handicap reverseSideHandicap = iHandicapService.findHandicap(event.getCoinId(), matchItem.getClinchPrice(), reversedEntrustSide);
            buildUpdateHandicap(reverseSideHandicap, matchItem.getClinchQty().negate());
            iHandicapService.updateHandicapAndSyncCache(reverseSideHandicap);
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
