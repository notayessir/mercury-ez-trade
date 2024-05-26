package com.notayessir.user.user.controller.admin;


import com.notayessir.common.vo.BusinessResp;
import com.notayessir.common.vo.req.BasePageReq;
import com.notayessir.common.vo.resp.BasePageResp;
import com.notayessir.user.user.service.FacadeAccountService;
import com.notayessir.user.user.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping(value = "user-service/admin-api/account/")
@RestController
public class AdminAccountController {

    @Autowired
    private FacadeAccountService facadeAccountService;

    @PostMapping("v1/deposit")
    public BusinessResp<Void> apiDeposit(@RequestBody DepositReq req){
        req.checkAndInit();

        DepositResp resp = facadeAccountService.apiDeposit(req);

        return BusinessResp.ok();
    }


}
