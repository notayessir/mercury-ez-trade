package com.notayessir.user.user.controller.api;


import com.notayessir.common.vo.BusinessResp;
import com.notayessir.common.vo.req.BasePageReq;
import com.notayessir.common.vo.resp.BasePageResp;
import com.notayessir.user.user.service.FacadeAccountService;
import com.notayessir.user.user.vo.*;
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


    @PostMapping("v1/find-accounts")
    public BusinessResp<List<FindAccountResp>> apiFindAccounts(@RequestBody FindAccountReq req){

        List<FindAccountResp> resp = facadeAccountService.apiFindAccounts(req);

        return BusinessResp.ok(resp);
    }



    @PostMapping("v1/find-capital-records")
    public BusinessResp<BasePageResp<FindCapitalRecordResp>> apiFindCapitalRecords(@RequestBody BasePageReq<FindCapitalRecordReq> req){

        BasePageResp<FindCapitalRecordResp> resp = facadeAccountService.apiFindCapitalRecords(req);

        return BusinessResp.ok(resp);
    }


}
