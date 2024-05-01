package com.notayessir.user.user.controller.api;


import com.notayessir.common.vo.BusinessResp;
import com.notayessir.user.user.service.FacadeAccountService;
import com.notayessir.user.user.vo.DepositReq;
import com.notayessir.user.user.vo.DepositResp;
import com.notayessir.user.user.vo.FindAccountResp;
import com.notayessir.user.user.vo.FindAccountsReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(value = "user-service/api/account/")
@RestController
public class APIAccountController {

    @Autowired
    private FacadeAccountService facadeAccountService;

    @PostMapping("v1/deposit")
    public BusinessResp<Void> apiDeposit(@RequestBody DepositReq req){
        req.checkAndInit();

        DepositResp resp = facadeAccountService.apiDeposit(req);

        return BusinessResp.ok();
    }


    @GetMapping("v1/find-accounts")
    public BusinessResp<List<FindAccountResp>> apiFindAccounts(@RequestBody FindAccountsReq req){
        req.checkAndInit();

        List<FindAccountResp> resp = facadeAccountService.apiFindAccounts(req);

        return BusinessResp.ok(resp);
    }


}
