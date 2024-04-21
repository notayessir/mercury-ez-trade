package com.notayessir.quote.spot.handler.impl;

import com.notayessir.bo.MatchItemBO;
import com.notayessir.bo.MatchResultBO;
import com.notayessir.bo.OrderItemBO;
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


@Service
public class CancelHandicapHandler implements HandicapHandler {


    @Autowired
    private IHandicapService iHandicapService;

    @Override
    public void handleHandicapUpdatedEvent(MatchResultBO event) {
        OrderItemBO takerOrder = event.getTakerOrder();
        Integer entrustSide = takerOrder.getEntrustSide();
        Long coinId = event.getCoinId();
        // 1. deduct forward side remain qty if type is equal to normal limit order
        if (takerOrder.getEntrustType() == EnumEntrustType.NORMAL_LIMIT.getType()){
            BigDecimal remainEntrustQty = takerOrder.getRemainEntrustQty();
            Handicap forwardSideHandicap = iHandicapService.findHandicap(coinId, takerOrder.getEntrustPrice(), entrustSide);
            buildUpdateHandicap(forwardSideHandicap, remainEntrustQty.negate());

            iHandicapService.updateHandicapAndSyncCache(forwardSideHandicap);

        } else {
            // 2. market order, premium limit order logic, deduct reverse side qty
            List<MatchItemBO> matchItems = event.getMatchItems();
            for (MatchItemBO matchItem : matchItems) {
                int reversedEntrustSide = EnumEntrustSide.reverseEntrustSide(entrustSide).getCode();
                Handicap reverseSideHandicap = iHandicapService.findHandicap(event.getCoinId(), matchItem.getClinchPrice(), reversedEntrustSide);
                buildUpdateHandicap(reverseSideHandicap, matchItem.getClinchQty().negate());
                iHandicapService.updateHandicapAndSyncCache(reverseSideHandicap);
            }
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

}
