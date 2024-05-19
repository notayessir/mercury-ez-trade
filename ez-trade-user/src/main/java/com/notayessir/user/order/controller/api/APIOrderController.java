package com.notayessir.user.order.controller.api;


import com.notayessir.common.vo.BusinessResp;
import com.notayessir.common.vo.req.BasePageReq;
import com.notayessir.common.vo.resp.BasePageResp;
import com.notayessir.user.order.service.FacadeOrderService;
import com.notayessir.user.order.vo.PatchOrderReq;
import com.notayessir.user.order.vo.CreateOrderReq;
import com.notayessir.user.order.vo.FindOrderReq;
import com.notayessir.user.order.vo.PatchOrderResp;
import com.notayessir.user.order.vo.CreateOrderResp;
import com.notayessir.user.order.vo.FindOrderResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping(value = "user-service/api/order")
@RestController
public class APIOrderController {

    @Autowired
    private FacadeOrderService facadeOrderService;


    @PostMapping(value = "v1/create-order")
    public BusinessResp<CreateOrderResp> createOrder(@RequestBody CreateOrderReq req){
        req.checkAndInit();
        CreateOrderResp resp = facadeOrderService.apiCreateOrder(req);

        return BusinessResp.ok(resp);
    }

    @PostMapping(value = "v1/cancel-order")
    public BusinessResp<Void> cancelOrder(@RequestBody PatchOrderReq req){
        req.checkAndInit();

        PatchOrderResp resp = facadeOrderService.apiCancelOrder(req);

        return BusinessResp.ok();
    }

    @PostMapping(value = "v1/find-order")
    public BusinessResp<FindOrderResp> findOrder(@RequestBody FindOrderReq req){
        req.checkAndInit();

        FindOrderResp resp = facadeOrderService.apiFindOrder(req);

        return BusinessResp.ok(resp);
    }

    @PostMapping(value = "v1/find-orders")
    public BusinessResp<BasePageResp<FindOrderResp>> findOrders(@RequestBody BasePageReq<FindOrderReq> req){

        BasePageResp<FindOrderResp> resp = facadeOrderService.apiFindOrders(req);

        return BusinessResp.ok(resp);
    }







}
