package com.notayessir.user.user.service;

import com.notayessir.common.constant.EnumRequestSource;
import com.notayessir.user.user.bo.DepositReqBO;
import com.notayessir.user.user.bo.DepositRespBO;
import com.notayessir.user.user.constant.EnumCapitalBusinessCode;
import com.notayessir.user.user.entity.Account;
import com.notayessir.user.user.vo.DepositReq;
import com.notayessir.user.user.vo.DepositResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FacadeAccountService {


    @Autowired
    private IAccountService iAccountService;


    @Autowired
    private ICapitalRecordService iCapitalRecordService;

    @Transactional
    public DepositRespBO deposit(DepositReqBO reqBO) {
        Account account = iAccountService.createAccountIfAbsent(reqBO.getUserId(), reqBO.getCurrency());
        // 1. increase available number
        iAccountService.addAvailable(account, reqBO.getAvailable());

        // 2. create a capital record
        iCapitalRecordService.createCapitalFlowIn(reqBO.getUserId(), reqBO.getCurrency(), reqBO.getAvailable().toString(), EnumCapitalBusinessCode.DEPOSIT);
        return new DepositRespBO();

    }

    public DepositResp adminDeposit(DepositReq req) {
        DepositReqBO reqBO = toDepositReqBO(req);

        DepositRespBO respBO = deposit(reqBO);

        return toDepositResp(respBO);
    }

    private DepositResp toDepositResp(DepositRespBO respBO) {
        return new DepositResp();
    }

    private DepositReqBO toDepositReqBO(DepositReq req) {
        DepositReqBO reqBO = new DepositReqBO();
        reqBO.setIp(req.getIp());
        reqBO.setCurrency(req.getCurrency());
        reqBO.setAvailable(req.getAvailable());
        reqBO.setUserId(req.getUserId());
        reqBO.setRequestId(req.getRequestId());
        reqBO.setRequestSource(EnumRequestSource.ADMIN);
        return reqBO;
    }
}
