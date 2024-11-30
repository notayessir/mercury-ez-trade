package com.notayessir.quote.spot.handler.impl;

import com.notayessir.engine.api.bo.MatchItemBO;
import com.notayessir.engine.api.bo.MatchResultBO;
import com.notayessir.engine.api.bo.OrderItemBO;
import com.notayessir.engine.api.constant.EnumEntrustSide;
import com.notayessir.engine.api.constant.EnumEntrustType;
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
            iHandicapService.updateHandicap(forwardSideHandicap);
        } else {
            // 2. market order, premium limit order, deduct reverse side qty
            List<MatchItemBO> matchItems = event.getMatchItems();
            for (MatchItemBO matchItem : matchItems) {
                int reversedEntrustSide = EnumEntrustSide.reverseEntrustSide(entrustSide).getCode();
                Handicap reverseSideHandicap = iHandicapService.findHandicap(event.getCoinId(), matchItem.getClinchPrice(), reversedEntrustSide);
                buildUpdateHandicap(reverseSideHandicap, matchItem.getClinchQty().negate());
                iHandicapService.updateHandicap(reverseSideHandicap);
            }
        }
    }



    private void buildUpdateHandicap(Handicap handicap, BigDecimal toClinchQty) {
        BigDecimal qty = new BigDecimal(handicap.getQty()).add(toClinchQty);

        handicap.setQty(qty.toString());
        handicap.setUpdateTime(LocalDateTime.now());
    }

}
