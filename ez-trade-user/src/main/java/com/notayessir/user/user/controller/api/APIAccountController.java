package com.notayessir.user.user.controller.api;


import com.notayessir.common.vo.BusinessResp;
import com.notayessir.user.user.service.FacadeAccountService;
import com.notayessir.user.user.vo.DepositReq;
import com.notayessir.user.user.vo.DepositResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = "user-service/admin-api/account/v1/")
@RestController
public class APIAccountController {

    @Autowired
    private FacadeAccountService facadeAccountService;

    @PostMapping("deposit")
    public BusinessResp<Void> adminDeposit(@RequestBody DepositReq req){
        req.checkAndInit();

        DepositResp resp = facadeAccountService.adminDeposit(req);

        return BusinessResp.ok();
    }


}
