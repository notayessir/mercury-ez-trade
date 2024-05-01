package com.notayessir.user.user.service;

import cn.hutool.core.collection.CollectionUtil;
import com.notayessir.common.constant.EnumRequestSource;
import com.notayessir.common.vo.resp.BasePageResp;
import com.notayessir.user.user.bo.DepositReqBO;
import com.notayessir.user.user.bo.DepositRespBO;
import com.notayessir.user.user.bo.FindAccountsBO;
import com.notayessir.user.user.bo.FindAccountRespBO;
import com.notayessir.user.user.constant.EnumCapitalBusinessCode;
import com.notayessir.user.user.entity.Account;
import com.notayessir.user.user.vo.DepositReq;
import com.notayessir.user.user.vo.DepositResp;
import com.notayessir.user.user.vo.FindAccountsReq;
import com.notayessir.user.user.vo.FindAccountResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    public DepositResp apiDeposit(DepositReq req) {
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

    public List<FindAccountResp> apiFindAccounts(FindAccountsReq req) {
        FindAccountsBO reqBO = toViewAssetReqBO(req);

        List<FindAccountRespBO> resp = findAccounts(reqBO);

        return toListFindAccountResp(resp);
    }

    private List<FindAccountResp> toListFindAccountResp(List<FindAccountRespBO> resp) {
        List<FindAccountResp> list = new ArrayList<>();
        if (CollectionUtil.isEmpty(resp)){
            return Collections.emptyList();
        }
        for (FindAccountRespBO source : resp) {
            FindAccountResp item = new FindAccountResp();
            item.setHold(source.getHold());
            item.setCurrency(source.getCurrency());
            item.setAvailable(source.getAvailable());
            item.setUserId(source.getUserId());

            list.add(item);
        }
        return list;
    }

    private List<FindAccountRespBO> findAccounts(FindAccountsBO reqBO) {
        List<Account> accounts = iAccountService.getAccounts(reqBO.getUserId());
        return toListFindAccountRespBO(accounts);
    }

    private List<FindAccountRespBO> toListFindAccountRespBO(List<Account> accounts) {
        List<FindAccountRespBO> list = new ArrayList<>();
        if (CollectionUtil.isEmpty(accounts)){
            return Collections.emptyList();
        }
        for (Account account : accounts) {
            FindAccountRespBO item = new FindAccountRespBO();
            item.setHold(account.getHold());
            item.setCurrency(account.getCurrency());
            item.setAvailable(account.getAvailable());
            item.setUserId(account.getUserId());

            list.add(item);
        }
        return list;
    }


    private FindAccountsBO toViewAssetReqBO(FindAccountsReq req) {
        FindAccountsBO bo = new FindAccountsBO();
        bo.setIp(req.getIp());
        bo.setRequestId(req.getRequestId());
        bo.setUserId(req.getUserId());
        bo.setRequestSource(EnumRequestSource.API);

        return bo;
    }
}
