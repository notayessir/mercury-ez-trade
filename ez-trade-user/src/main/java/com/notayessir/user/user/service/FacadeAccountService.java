package com.notayessir.user.user.service;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.notayessir.common.constant.EnumRequestSource;
import com.notayessir.common.vo.req.BasePageReq;
import com.notayessir.common.vo.resp.BasePageResp;
import com.notayessir.user.order.entity.Order;
import com.notayessir.user.user.bo.*;
import com.notayessir.user.user.constant.EnumCapitalBusinessCode;
import com.notayessir.user.user.entity.Account;
import com.notayessir.user.user.entity.CapitalRecord;
import com.notayessir.user.user.vo.*;
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

    public List<FindAccountResp> apiFindAccounts(FindAccountReq req) {
        FindAccountReqBO reqBO = toFindAccountsBO(req);

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

    private List<FindAccountRespBO> findAccounts(FindAccountReqBO reqBO) {
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


    private FindAccountReqBO toFindAccountsBO(FindAccountReq req) {
        FindAccountReqBO bo = new FindAccountReqBO();
        bo.setIp(req.getIp());
        bo.setRequestId(req.getRequestId());
        bo.setUserId(req.getUserId());
        bo.setRequestSource(EnumRequestSource.API);

        return bo;
    }

    public BasePageResp<FindCapitalRecordResp> apiFindCapitalRecords(BasePageReq<FindCapitalRecordReq> req) {


        BasePageReq<FindCapitalRecordReqBO> reqBO = toPageFindCapitalRecordReqBO(req);

        Page<CapitalRecord> pages = iCapitalRecordService.findCapitalRecords(reqBO);

        return toPageFindCapitalRecordResp(pages);
    }

    private BasePageResp<FindCapitalRecordResp> toPageFindCapitalRecordResp(Page<CapitalRecord> page) {
        if (CollectionUtil.isEmpty(page.getRecords())){
            return new BasePageResp<>();
        }

        BasePageResp<FindCapitalRecordResp> basePageResp = new BasePageResp<>(page.getTotal(), page.getSize(), page.getCurrent());
        List<FindCapitalRecordResp> list = toListFindCapitalRecordResp(page.getRecords());
        basePageResp.setRecords(list);
        return basePageResp;
    }

    private BasePageReq<FindCapitalRecordReqBO> toPageFindCapitalRecordReqBO(BasePageReq<FindCapitalRecordReq> req) {
        BasePageReq<FindCapitalRecordReqBO> pageReq = new BasePageReq<>();
        pageReq.setPageSize(req.getPageSize());
        pageReq.setPageNum(req.getPageNum());
        pageReq.setIp(req.getIp());
        pageReq.setUserId(req.getUserId());
        pageReq.setRequestSource(EnumRequestSource.API);
        pageReq.setRequestId(req.getRequestId());


        FindCapitalRecordReqBO reqBO = toFindCapitalRecordReqBO(req.getQuery());
        pageReq.setQuery(reqBO);

        return pageReq;
    }

    private List<FindCapitalRecordResp> toListFindCapitalRecordResp(List<CapitalRecord> capitalRecords) {
        if (CollectionUtil.isEmpty(capitalRecords)){
            return Collections.emptyList();
        }
        List<FindCapitalRecordResp> list = new ArrayList<>(capitalRecords.size());
        for (CapitalRecord capitalRecord : capitalRecords) {
            FindCapitalRecordResp target = new FindCapitalRecordResp();
            target.setId(capitalRecord.getId());
            target.setNum(capitalRecord.getNum());
            target.setDirection(capitalRecord.getDirection());
            target.setBusinessCode(capitalRecord.getBusinessCode());
            EnumCapitalBusinessCode businessCode = EnumCapitalBusinessCode.getByCode(capitalRecord.getBusinessCode());
            target.setBusinessCodeDesc(businessCode.getDesc());
            target.setCreateTime(capitalRecord.getCreateTime());
            target.setCurrency(capitalRecord.getCurrency());
            list.add(target);
        }
        return list;
    }

    private FindCapitalRecordReqBO toFindCapitalRecordReqBO(FindCapitalRecordReq req) {
        FindCapitalRecordReqBO reqBO = new FindCapitalRecordReqBO();
        reqBO.setIp(req.getIp());
        reqBO.setRequestId(req.getRequestId());
        reqBO.setRequestSource(EnumRequestSource.API);
        reqBO.setUserId(req.getUserId());
        reqBO.setCurrency(req.getCurrency());
        return reqBO;
    }
}
