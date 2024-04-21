package com.notayessir.user.coin.controller.admin;


import com.notayessir.common.vo.BusinessResp;
import com.notayessir.user.coin.service.FacadeCoinService;
import com.notayessir.user.coin.vo.CreateCoinReq;
import com.notayessir.user.coin.vo.CreateCoinResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = "user-service/admin-api/coin/v1/")
@RestController
public class AdminCoinController {


    @Autowired
    private FacadeCoinService facadeCoinService;

    @PostMapping("create-coin")
    public BusinessResp<CreateCoinResp> adminCreateCoinPair(@RequestBody CreateCoinReq req){
        req.checkAndInit();

        CreateCoinResp resp = facadeCoinService.adminCreateCoinPair(req);

        return BusinessResp.ok(resp);
    }



}
