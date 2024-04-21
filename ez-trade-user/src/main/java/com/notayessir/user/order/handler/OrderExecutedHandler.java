package com.notayessir.user.order.handler;

import com.notayessir.bo.MatchResultBO;
import com.notayessir.common.web.EZAppContext;
import com.notayessir.constant.EnumMatchCommand;
import com.notayessir.constant.EnumMatchStatus;
import com.notayessir.user.order.handler.impl.OrderCancelledHandler;
import com.notayessir.user.order.handler.impl.OrderFilledHandler;
import com.notayessir.user.order.handler.impl.OrderNewHandler;

public interface OrderExecutedHandler {

    void handle(MatchResultBO event);

    static OrderExecutedHandler getCommandExecutedHandler(int commandType){
        if (commandType == EnumMatchStatus.CANCEL.getStatus()){
            return EZAppContext.getBean(OrderCancelledHandler.class);
        } else if (commandType == EnumMatchStatus.NEW.getStatus()){
            return EZAppContext.getBean(OrderNewHandler.class);
        } else {
            // FILLED
            return EZAppContext.getBean(OrderFilledHandler.class);
        }
    }

}
