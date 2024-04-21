package com.notayessir.user.order.controller.api;


import com.notayessir.common.vo.BusinessResp;
import com.notayessir.common.vo.req.BasePageReq;
import com.notayessir.common.vo.resp.BasePageResp;
import com.notayessir.user.order.service.FacadeOrderService;
import com.notayessir.user.order.vo.PatchOrderReq;
import com.notayessir.user.order.vo.CreateOrderReq;
import com.notayessir.user.order.vo.FindOrderReq;
import com.notayessir.user.order.vo.FindOrdersReq;
import com.notayessir.user.order.vo.PatchOrderResp;
import com.notayessir.user.order.vo.CreateOrderResp;
import com.notayessir.user.order.vo.FindOrderResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping(value = "user-service/api/order/v1")
@RestController
public class APIOrderController {

    @Autowired
    private FacadeOrderService facadeOrderService;


    @PostMapping(value = "create-order")
    public BusinessResp<CreateOrderResp> createOrder(@RequestBody CreateOrderReq req){
        req.checkAndInit();
        CreateOrderResp resp = facadeOrderService.apiCreateOrder(req);

        return BusinessResp.ok(resp);
    }

    @PostMapping(value = "cancel-order")
    public BusinessResp<Void> cancelOrder(@RequestBody PatchOrderReq req){
        req.checkAndInit();

        PatchOrderResp resp = facadeOrderService.apiCancelOrder(req);

        return BusinessResp.ok();
    }

    @GetMapping(value = "find-order")
    public BusinessResp<FindOrderResp> findOrder(@RequestBody FindOrderReq req){
        req.checkAndInit();

        FindOrderResp resp = facadeOrderService.apiFindOrder(req);

        return BusinessResp.ok(resp);
    }

    @GetMapping(value = "find-orders")
    public BusinessResp<BasePageResp<FindOrderResp>> findOrders(@RequestBody BasePageReq<FindOrdersReq> req){

        BasePageResp<FindOrderResp> resp = facadeOrderService.apiFindOrders(req);

        return BusinessResp.ok(resp);
    }







}