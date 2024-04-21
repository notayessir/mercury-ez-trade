package com.notayessir.queue.route.controller.admin;


import com.notayessir.common.vo.BusinessResp;
import com.notayessir.queue.route.service.FacadeRouteService;
import com.notayessir.queue.route.vo.CreateRouteInfoReq;
import com.notayessir.queue.route.vo.CreateRouteInfoResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = "queue-service/admin-api/route/v1/")
@RestController
public class AdminRouteInfoController {

    @Autowired
    private FacadeRouteService facadeRouteService;

    @PostMapping(value = "create-route")
    public BusinessResp<CreateRouteInfoResp> createRoute(@RequestBody CreateRouteInfoReq req){
        req.checkAndInit();

        CreateRouteInfoResp resp = facadeRouteService.adminCreateRouteInfo(req);

        return BusinessResp.ok(resp);
    }


}
