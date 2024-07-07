package com.notayessir.user.user.controller.api;


import com.notayessir.common.vo.BusinessResp;
import com.notayessir.common.vo.req.BasePageReq;
import com.notayessir.common.vo.resp.BasePageResp;
import com.notayessir.user.user.bo.TokenPayloadBO;
import com.notayessir.user.user.service.FacadeAccountService;
import com.notayessir.user.user.service.UserTokenService;
import com.notayessir.user.user.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(value = "user-service/api/account/")
@RestController
public class APIAccountController {

    @Autowired
    private FacadeAccountService facadeAccountService;
    @Autowired
    private UserTokenService userTokenService;


    @PostMapping("v1/find-accounts")
    public BusinessResp<List<FindAccountResp>> apiFindAccounts(@RequestHeader("Authorization") String token,
                                                               @RequestBody FindAccountReq req){
        TokenPayloadBO payload = userTokenService.getPayload(token);
        req.setUserId(payload.getSub());
        List<FindAccountResp> resp = facadeAccountService.apiFindAccounts(req);

        return BusinessResp.ok(resp);
    }



    @PostMapping("v1/find-capital-records")
    public BusinessResp<BasePageResp<FindCapitalRecordResp>> apiFindCapitalRecords(@RequestHeader("Authorization") String token,
                                                                                   @RequestBody BasePageReq<FindCapitalRecordReq> req){
        TokenPayloadBO payload = userTokenService.getPayload(token);
        req.setUserId(payload.getSub());
        BasePageResp<FindCapitalRecordResp> resp = facadeAccountService.apiFindCapitalRecords(req);

        return BusinessResp.ok(resp);
    }


}
