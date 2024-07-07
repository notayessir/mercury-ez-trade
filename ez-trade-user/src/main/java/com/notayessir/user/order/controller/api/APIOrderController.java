package com.notayessir.user.order.controller.api;


import com.notayessir.common.vo.BusinessResp;
import com.notayessir.common.vo.req.BasePageReq;
import com.notayessir.common.vo.resp.BasePageResp;
import com.notayessir.user.order.service.FacadeOrderService;
import com.notayessir.user.order.vo.CancelOrderReq;
import com.notayessir.user.order.vo.CreateOrderReq;
import com.notayessir.user.order.vo.FindOrderReq;
import com.notayessir.user.order.vo.CancelOrderResp;
import com.notayessir.user.order.vo.CreateOrderResp;
import com.notayessir.user.order.vo.FindOrderResp;
import com.notayessir.user.user.bo.TokenPayloadBO;
import com.notayessir.user.user.service.UserTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping(value = "user-service/api/order")
@RestController
public class APIOrderController {

    @Autowired
    private FacadeOrderService facadeOrderService;

    @Autowired
    private UserTokenService userTokenService;


    @PostMapping(value = "v1/create-order")
    public BusinessResp<CreateOrderResp> createOrder(@RequestHeader("Authorization") String token,
                                                     @RequestBody CreateOrderReq req){


        TokenPayloadBO payload = userTokenService.getPayload(token);
        req.setUserId(payload.getSub());

        req.checkAndInit();


        CreateOrderResp resp = facadeOrderService.apiCreateOrder(req);

        return BusinessResp.ok(resp);
    }

    @PostMapping(value = "v1/cancel-order")
    public BusinessResp<Void> cancelOrder(@RequestHeader("Authorization") String token,
                                          @RequestBody CancelOrderReq req){
        TokenPayloadBO payload = userTokenService.getPayload(token);
        req.setUserId(payload.getSub());
        req.checkAndInit();

        CancelOrderResp resp = facadeOrderService.apiCancelOrder(req);

        return BusinessResp.ok();
    }

    @PostMapping(value = "v1/find-order")
    public BusinessResp<FindOrderResp> findOrder(@RequestHeader("Authorization") String token,
                                                 @RequestBody FindOrderReq req){
        TokenPayloadBO payload = userTokenService.getPayload(token);
        req.setUserId(payload.getSub());
        req.checkAndInit();

        FindOrderResp resp = facadeOrderService.apiFindOrder(req);

        return BusinessResp.ok(resp);
    }

    @PostMapping(value = "v1/find-orders")
    public BusinessResp<BasePageResp<FindOrderResp>> findOrders(@RequestHeader("Authorization") String token,
                                                                @RequestBody BasePageReq<FindOrderReq> req){
        TokenPayloadBO payload = userTokenService.getPayload(token);
        req.setUserId(payload.getSub());
        BasePageResp<FindOrderResp> resp = facadeOrderService.apiFindOrders(req);

        return BusinessResp.ok(resp);
    }







}
