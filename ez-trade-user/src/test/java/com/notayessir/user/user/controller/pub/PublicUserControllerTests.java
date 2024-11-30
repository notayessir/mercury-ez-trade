package com.notayessir.user.user.controller.pub;


import com.notayessir.common.vo.BusinessResp;
import com.notayessir.common.vo.BusinessRespCode;
import com.notayessir.user.user.vo.RegisterReq;
import com.notayessir.user.user.vo.RegisterResp;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Objects;

@SpringBootTest(classes = PublicUserController.class)
public class PublicUserControllerTests {

    @Autowired
    private PublicUserController controller;


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
