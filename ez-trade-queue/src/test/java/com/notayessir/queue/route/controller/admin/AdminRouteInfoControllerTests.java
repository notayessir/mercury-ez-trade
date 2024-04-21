package com.notayessir.queue.route.controller.admin;


import com.notayessir.common.vo.BusinessResp;
import com.notayessir.common.vo.BusinessRespCode;
import com.notayessir.queue.route.vo.CreateRouteInfoReq;
import com.notayessir.queue.route.vo.CreateRouteInfoResp;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Objects;

@SpringBootTest
public class AdminRouteInfoControllerTests {

    @Autowired
    private AdminRouteInfoController controller;


    @Test
    void contextLoads() throws Exception {
        Assumptions.assumeTrue(Objects.nonNull(controller));
    }


    @Test
    public void createRouteTest(){
        CreateRouteInfoReq req = new CreateRouteInfoReq();
        req.setRouteTo("default");
        req.setCoinId(20L);
        BusinessResp<CreateRouteInfoResp> resp = controller.createRoute(req);
        Assumptions.assumeTrue(StringUtils.equals(resp.getCode(), BusinessRespCode.SUCCESS.getCode()));
        Assumptions.assumeTrue(Objects.nonNull(resp.getData().getId()));
    }

}
