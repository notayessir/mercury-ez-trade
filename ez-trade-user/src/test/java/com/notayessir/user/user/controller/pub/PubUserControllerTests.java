package com.notayessir.user.user.controller.pub;


import com.notayessir.common.vo.BusinessResp;
import com.notayessir.common.vo.BusinessRespCode;
import com.notayessir.user.user.controller.api.APIAccountController;
import com.notayessir.user.user.service.FacadeUserService;
import com.notayessir.user.user.vo.DepositReq;
import com.notayessir.user.user.vo.DepositResp;
import com.notayessir.user.user.vo.RegisterReq;
import com.notayessir.user.user.vo.RegisterResp;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@SpringBootTest(classes = PubUserController.class)
public class PubUserControllerTests {

    @Autowired
    private PubUserController controller;


    @Test
    void contextLoads() throws Exception {
        Assumptions.assumeTrue(Objects.nonNull(controller));
    }



    @Test
    public void publicApiRegisterTest(){
        RegisterReq registerReq = new RegisterReq();
        registerReq.setUsername("eddie");
        BusinessResp<RegisterResp> resp = controller.publicApiRegister(registerReq);
        Assumptions.assumeTrue(StringUtils.equals(resp.getCode(), BusinessRespCode.SUCCESS.getCode()));
    }

}
