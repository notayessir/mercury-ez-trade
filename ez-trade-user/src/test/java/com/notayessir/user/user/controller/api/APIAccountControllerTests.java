package com.notayessir.user.user.controller.api;


import cn.hutool.core.util.RandomUtil;
import com.notayessir.common.vo.BusinessResp;
import com.notayessir.common.vo.BusinessRespCode;
import com.notayessir.user.order.service.FacadeOrderService;
import com.notayessir.user.user.vo.DepositReq;
import com.notayessir.user.user.vo.DepositResp;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Objects;


@SpringBootTest(classes = APIAccountController.class)
public class APIAccountControllerTests {

    @Autowired
    private APIAccountController controller;


    @Test
    void contextLoads() throws Exception {
        Assumptions.assumeTrue(Objects.nonNull(controller));
    }

    @Test
    public void adminDepositTest(){
        DepositReq req = new DepositReq();
        req.setAvailable(new BigDecimal("10011"));
        req.setUserId(1774333046012248064L);
        req.setRequestId(RandomUtil.randomString(32));
        req.setCurrency("USDT");
        BusinessResp<Void> resp = controller.adminDeposit(req);
        Assumptions.assumeTrue(StringUtils.equals(resp.getCode(), BusinessRespCode.SUCCESS.getCode()));
    }


}
