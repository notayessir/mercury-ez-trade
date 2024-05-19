package com.notayessir.user.coin.service;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.notayessir.common.constant.EnumFieldDeleted;
import com.notayessir.common.constant.EnumRequestSource;
import com.notayessir.common.constant.EnumFieldVersion;
import com.notayessir.common.vo.req.BasePageReq;
import com.notayessir.common.vo.resp.BasePageResp;
import com.notayessir.user.coin.bo.CreateCoinReqBO;
import com.notayessir.user.coin.bo.CreateCoinRespBO;
import com.notayessir.user.coin.bo.FindCoinReqBO;
import com.notayessir.user.coin.bo.FindCoinRespBO;
import com.notayessir.user.coin.entity.CoinPair;
import com.notayessir.user.coin.vo.CreateCoinReq;
import com.notayessir.user.coin.vo.FindCoinReq;
import com.notayessir.user.coin.vo.CreateCoinResp;
import com.notayessir.user.coin.vo.FindCoinResp;
import com.notayessir.user.order.bo.FindOrderRespBO;
import com.notayessir.user.order.vo.FindOrderResp;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class FacadeCoinService {

    @Autowired
    private ICoinPairService iCoinPairService;


    public CreateCoinRespBO createCoinPair(CreateCoinReqBO reqBO) {
        CoinPair coinPair = buildCoinPair(reqBO);
        iCoinPairService.createCoin(coinPair);
        CreateCoinRespBO createCoinRespBO = new CreateCoinRespBO();
        createCoinRespBO.setId(coinPair.getId());
        return createCoinRespBO;
    }

    private CoinPair buildCoinPair(CreateCoinReqBO reqBO) {
        CoinPair coinPair = new CoinPair();
        coinPair.setBaseCurrency(reqBO.getBaseCurrency());
        coinPair.setBaseMaxQty(reqBO.getBaseMaxQty().toString());
        coinPair.setBaseMinQty(reqBO.getBaseMinQty().toString());
        coinPair.setBaseScale(reqBO.getBaseScale());

        coinPair.setQuoteCurrency(reqBO.getQuoteCurrency());
        coinPair.setQuoteScale(reqBO.getQuoteScale());
        coinPair.setQuoteMaxQty(reqBO.getQuoteMaxQty().toString());
        coinPair.setQuoteMinQty(reqBO.getQuoteMinQty().toString());
        coinPair.setId(IdUtil.getSnowflakeNextId());
        coinPair.setCreateTime(LocalDateTime.now());
        coinPair.setUpdateTime(LocalDateTime.now());
        coinPair.setVersion(EnumFieldVersion.INIT.getCode());
        coinPair.setDeleted(EnumFieldDeleted.NOT_DELETED.getCode());
        return coinPair;
    }



    private BasePageResp<FindCoinRespBO> toPageGetCoinResp(Page<CoinPair> page) {
        List<FindCoinRespBO> list = new ArrayList<>(page.getRecords().size());
        for (CoinPair coinPair : page.getRecords()){
            FindCoinRespBO target = new FindCoinRespBO();
            target.setBaseCurrency(coinPair.getBaseCurrency());
            target.setBaseMaxQty(coinPair.getBaseMaxQty());
            target.setBaseMinQty(coinPair.getBaseMinQty());
            target.setBaseScale(coinPair.getBaseScale());

            target.setQuoteCurrency(coinPair.getQuoteCurrency());
            target.setQuoteScale(coinPair.getQuoteScale());
            target.setQuoteMaxQty(coinPair.getQuoteMaxQty());
            target.setQuoteMinQty(coinPair.getQuoteMinQty());
            list.add(target);
        }

        BasePageResp<FindCoinRespBO> resp = new BasePageResp<>();
        resp.setCurrent(page.getCurrent());
        resp.setSize(page.getSize());
        resp.setTotal(page.getTotal());
        resp.setRecords(list);
        return resp;
    }

    public CreateCoinResp adminCreateCoinPair(CreateCoinReq req) {
        CreateCoinReqBO reqBO =  toCreateProductReqBO(req);

        CreateCoinRespBO respBO = createCoinPair(reqBO);

        return toCreateProductResp(respBO);
    }

    private CreateCoinResp toCreateProductResp(CreateCoinRespBO respBO) {
        CreateCoinResp createCoinResp = new CreateCoinResp();
        createCoinResp.setId(respBO.getId());
        return createCoinResp;
    }

    private CreateCoinReqBO toCreateProductReqBO(CreateCoinReq req) {
        CreateCoinReqBO reqBO = new CreateCoinReqBO();
        reqBO.setRequestSource(EnumRequestSource.ADMIN);

        reqBO.setBaseCurrency(req.getBaseCurrency());
        reqBO.setBaseScale(req.getBaseScale());
        reqBO.setBaseMaxQty(req.getBaseMaxQty());
        reqBO.setBaseMinQty(req.getBaseMinQty());

        reqBO.setQuoteCurrency(req.getQuoteCurrency());
        reqBO.setQuoteScale(req.getQuoteScale());
        reqBO.setQuoteMaxQty(req.getQuoteMaxQty());
        reqBO.setQuoteMinQty(req.getQuoteMinQty());

        return reqBO;
    }

    public BasePageResp<FindCoinResp> publicApiGetCoinPair(BasePageReq<FindCoinReq> pageReq) {
        BasePageReq<FindCoinReqBO> page = toBasePageReqGetCoinReqBO(pageReq);

        BasePageResp<FindCoinRespBO> pageResp = getCoinPair(page);

        return toBasePageRespGetProductResp(pageResp);
    }

    private BasePageResp<FindCoinResp> toBasePageRespGetProductResp(BasePageResp<FindCoinRespBO> pageResp) {
        BasePageResp<FindCoinResp> target = new BasePageResp<>();
        BeanUtils.copyProperties(pageResp, target);
        List<FindCoinRespBO> records = pageResp.getRecords();
        if (CollectionUtil.isNotEmpty(records)){
            List<FindCoinResp> list = new ArrayList<>(records.size());
            for (FindCoinRespBO record : records) {
                list.add(new FindCoinResp(record));
            }
            target.setRecords(list);
        }
        return target;
    }

    private BasePageResp<FindCoinRespBO> getCoinPair(BasePageReq<FindCoinReqBO> req) {
        Page<CoinPair> page = iCoinPairService.getCoins(req);

        BasePageResp<FindCoinRespBO> resp = new BasePageResp<>(page.getTotal(), page.getSize(), page.getCurrent());
        List<CoinPair> records = page.getRecords();
        if (CollectionUtil.isNotEmpty(records)){
            List<FindCoinRespBO> list = new ArrayList<>(records.size());
            for (CoinPair record : records) {
                list.add(new FindCoinRespBO(record));
            }
            resp.setRecords(list);
        }
        return resp;
    }

    private BasePageReq<FindCoinReqBO> toBasePageReqGetCoinReqBO(BasePageReq<FindCoinReq> pageReq) {
        BasePageReq<FindCoinReqBO> target = new BasePageReq<>();
        target.setPageNum(pageReq.getPageNum());
        target.setIp(pageReq.getIp());
        target.setUserId(pageReq.getUserId());
        target.setPageSize(pageReq.getPageSize());
        target.setRequestId(pageReq.getRequestId());

        FindCoinReqBO query = new FindCoinReqBO();
        query.setRequestSource(EnumRequestSource.PUBLIC_API);
        target.setQuery(query);
        return target;
    }


    public FindCoinResp publicApiGetCoinPair(FindCoinReq req) {
        FindCoinReqBO reqBO = toGetCoinReqBO(req);

        FindCoinRespBO respBO = getCoinPair(reqBO);

        return new FindCoinResp(respBO);
    }

    private FindCoinRespBO getCoinPair(FindCoinReqBO reqBO) {
        CoinPair coinPair = iCoinPairService.getById(reqBO.getId());
        return toGetCoinRespBO(coinPair);
    }

    private FindCoinRespBO toGetCoinRespBO(CoinPair product) {
        FindCoinRespBO target = new FindCoinRespBO();
        target.setId(product.getId());
        target.setBaseCurrency(product.getBaseCurrency());
        target.setBaseMaxQty(product.getBaseMaxQty());
        target.setBaseMinQty(product.getBaseMinQty());
        target.setBaseScale(product.getBaseScale());

        target.setQuoteCurrency(product.getQuoteCurrency());
        target.setQuoteScale(product.getQuoteScale());
        target.setQuoteMaxQty(product.getQuoteMaxQty());
        target.setQuoteMinQty(product.getQuoteMinQty());
        return target;
    }

    private FindCoinReqBO toGetCoinReqBO(FindCoinReq req) {
        FindCoinReqBO reqBO = new FindCoinReqBO();
        reqBO.setId(req.getId());
        reqBO.setRequestSource(EnumRequestSource.PUBLIC_API);
        return reqBO;
    }

}
