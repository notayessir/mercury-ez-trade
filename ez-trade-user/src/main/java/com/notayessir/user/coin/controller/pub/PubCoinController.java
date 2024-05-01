package com.notayessir.user.coin.controller.pub;


import com.notayessir.common.vo.BusinessResp;
import com.notayessir.common.vo.req.BasePageReq;
import com.notayessir.common.vo.resp.BasePageResp;
import com.notayessir.user.coin.service.FacadeCoinService;
import com.notayessir.user.coin.vo.GetCoinReq;
import com.notayessir.user.coin.vo.GetCoinResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping(value = "user-service/public-api/coin")
@RestController
public class PubCoinController {


    @Autowired
    private FacadeCoinService facadeCoinService;


    @GetMapping("v1/get-coins")
    public BusinessResp<BasePageResp<GetCoinResp>> publicApiGetCoinPair(@RequestBody BasePageReq<GetCoinReq> req){
        GetCoinReq query = req.getQuery();
        query.checkAndInit();

        BasePageResp<GetCoinResp> page = facadeCoinService.publicApiGetCoinPair(req);

        return BusinessResp.ok(page);
    }



    @GetMapping("v1/get-coin")
    public BusinessResp<GetCoinResp> publicApiGetCoinPair(@RequestBody GetCoinReq req){
        req.checkAndInit();
        GetCoinResp resp = facadeCoinService.publicApiGetCoinPair(req);
        return BusinessResp.ok(resp);
    }

}
