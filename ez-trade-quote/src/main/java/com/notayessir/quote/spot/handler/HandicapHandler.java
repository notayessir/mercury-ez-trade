package com.notayessir.quote.spot.handler;

import com.notayessir.common.web.EZAppContext;
import com.notayessir.engine.api.bo.MatchResultBO;
import com.notayessir.engine.api.constant.EnumMatchStatus;
import com.notayessir.quote.spot.handler.impl.CancelHandicapHandler;
import com.notayessir.quote.spot.handler.impl.FilledHandicapHandler;
import com.notayessir.quote.spot.handler.impl.NewHandicapHandler;

public interface HandicapHandler {

    static HandicapHandler getInstance(Integer matchStatus){
        if (matchStatus == EnumMatchStatus.CANCEL.getStatus()){
            return EZAppContext.getBean(CancelHandicapHandler.class);
        } else if (matchStatus == EnumMatchStatus.NEW.getStatus()){
            return EZAppContext.getBean(NewHandicapHandler.class);
        } else {
            // FILLED
            return EZAppContext.getBean(FilledHandicapHandler.class);
        }
    }


    void handleHandicapUpdatedEvent(MatchResultBO event);


//    List<OrderBookDTO> handleHandicapUpdatedEventWithReturn(MatchResultBO event);
}
