package com.notayessir.quote.spot.controller.pub;


import com.notayessir.common.vo.BusinessResp;
import com.notayessir.quote.spot.service.FacadeSpotQuoteService;
import com.notayessir.quote.spot.vo.GetKlineReq;
import com.notayessir.quote.spot.vo.GetKlineResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Slf4j
@RequestMapping(value = "quote-service/public-api/kline/")
@RestController
public class PublicKlineController {


    @Autowired
    private FacadeSpotQuoteService facadeSpotQuoteService;

    @GetMapping(value = "v1/find-kline")
    public BusinessResp<List<GetKlineResp>> findKline(GetKlineReq req){
        req.checkAndInit();

        List<GetKlineResp> resp = facadeSpotQuoteService.publicApiFindKline(req);

        return BusinessResp.ok(resp);
    }


}
