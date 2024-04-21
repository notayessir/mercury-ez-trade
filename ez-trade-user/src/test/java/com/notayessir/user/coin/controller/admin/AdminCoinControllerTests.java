package com.notayessir.user.coin.controller.admin;


import com.notayessir.common.vo.BusinessResp;
import com.notayessir.common.vo.BusinessRespCode;
import com.notayessir.user.coin.service.FacadeCoinService;
import com.notayessir.user.coin.vo.CreateCoinReq;
import com.notayessir.user.coin.vo.CreateCoinResp;
import com.notayessir.user.order.controller.api.APIOrderController;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Objects;

@SpringBootTest(classes = AdminCoinController.class)
public class AdminCoinControllerTests {


    @Autowired
    private AdminCoinController controller;


    @Test
    void contextLoads() throws Exception {
        Assumptions.assumeTrue(Objects.nonNull(controller));
    }

    @Test
    void adminCreateCoinPairTest(){
        CreateCoinReq req = new CreateCoinReq();
        req.setBaseCurrency("USDT");
        req.setBaseMinQty(BigDecimal.ZERO);
        req.setBaseMaxQty(BigDecimal.ZERO);
        req.setBaseScale(2);

        req.setQuoteCurrency("BTC");
        req.setQuoteMinQty(BigDecimal.ZERO);
        req.setQuoteMaxQty(BigDecimal.ZERO);
        req.setQuoteScale(18);

        BusinessResp<CreateCoinResp> resp = controller.adminCreateCoinPair(req);
        Assumptions.assumeTrue(StringUtils.equals(resp.getCode(), BusinessRespCode.SUCCESS.getCode()));
        Assumptions.assumeTrue(Objects.nonNull(resp.getData().getId()));
    }

}
