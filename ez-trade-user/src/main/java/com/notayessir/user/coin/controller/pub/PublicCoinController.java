package com.notayessir.user.coin.controller.pub;


import com.notayessir.common.vo.BusinessResp;
import com.notayessir.common.vo.req.BasePageReq;
import com.notayessir.common.vo.resp.BasePageResp;
import com.notayessir.user.coin.service.FacadeCoinService;
import com.notayessir.user.coin.vo.FindCoinReq;
import com.notayessir.user.coin.vo.FindCoinResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping(value = "user-service/public-api/coin/")
@RestController
public class PublicCoinController {


    @Autowired
    private FacadeCoinService facadeCoinService;


    @PostMapping("v1/find-coins")
    public BusinessResp<BasePageResp<FindCoinResp>> publicApiGetCoinPairs(@RequestBody BasePageReq<FindCoinReq> req){

        BasePageResp<FindCoinResp> page = facadeCoinService.publicApiGetCoinPair(req);

        return BusinessResp.ok(page);
    }



    @PostMapping("v1/find-coin")
    public BusinessResp<FindCoinResp> publicApiGetCoinPair(@RequestBody FindCoinReq req){
        req.checkAndInit();
        FindCoinResp resp = facadeCoinService.publicApiGetCoinPair(req);
        return BusinessResp.ok(resp);
    }

}
