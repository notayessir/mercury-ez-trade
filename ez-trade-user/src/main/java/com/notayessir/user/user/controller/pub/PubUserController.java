package com.notayessir.user.user.controller.pub;


import com.notayessir.common.vo.BusinessResp;
import com.notayessir.user.user.service.FacadeUserService;
import com.notayessir.user.user.vo.RegisterReq;
import com.notayessir.user.user.vo.RegisterResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = "user-service/public-api/user/v1/")
@RestController
public class PubUserController {

    @Autowired
    private FacadeUserService facadeUserService;


    @PostMapping("register")
    public BusinessResp<RegisterResp> publicApiRegister(@RequestBody RegisterReq req){
        req.checkAndInit();
        RegisterResp resp = facadeUserService.publicApiRegister(req);

        return BusinessResp.ok(resp);
    }

}
