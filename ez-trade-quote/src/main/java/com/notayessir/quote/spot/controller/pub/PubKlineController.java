package com.notayessir.quote.spot.controller.pub;


import com.notayessir.common.vo.BusinessResp;
import com.notayessir.common.vo.req.BasePageReq;
import com.notayessir.common.vo.resp.BasePageResp;
import com.notayessir.quote.spot.service.FacadeSpotQuoteService;
import com.notayessir.quote.spot.vo.GetKlineReq;
import com.notayessir.quote.spot.vo.GetKlineResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RequestMapping(value = "quote-service/public-api/kline/v1/")
@RestController
public class PubKlineController {


    @Autowired
    private FacadeSpotQuoteService facadeSpotQuoteService;

    @GetMapping(value = "get-kline")
    public BusinessResp<BasePageResp<GetKlineResp>> getKline(@RequestBody BasePageReq<GetKlineReq> req){
        req.getQuery().checkAndInit();

        BasePageResp<GetKlineResp> resp = facadeSpotQuoteService.publicApiFindKline(req);

        return BusinessResp.ok(resp);
    }


}
