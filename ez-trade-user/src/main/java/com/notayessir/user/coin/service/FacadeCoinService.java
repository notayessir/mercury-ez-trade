package com.notayessir.user.coin.service;


import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.notayessir.common.constant.EnumFieldDeleted;
import com.notayessir.common.constant.EnumRequestSource;
import com.notayessir.common.constant.EnumFieldVersion;
import com.notayessir.common.vo.req.BasePageReq;
import com.notayessir.common.vo.resp.BasePageResp;
import com.notayessir.user.coin.bo.CreateCoinReqBO;
import com.notayessir.user.coin.bo.CreateCoinRespBO;
import com.notayessir.user.coin.bo.GetCoinReqBO;
import com.notayessir.user.coin.bo.GetCoinRespBO;
import com.notayessir.user.coin.entity.CoinPair;
import com.notayessir.user.coin.vo.CreateCoinReq;
import com.notayessir.user.coin.vo.GetCoinReq;
import com.notayessir.user.coin.vo.CreateCoinResp;
import com.notayessir.user.coin.vo.GetCoinResp;
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



    private BasePageResp<GetCoinRespBO> toPageGetCoinResp(Page<CoinPair> page) {
        List<GetCoinRespBO> list = new ArrayList<>(page.getRecords().size());
        for (CoinPair coinPair : page.getRecords()){
            GetCoinRespBO target = new GetCoinRespBO();
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

        BasePageResp<GetCoinRespBO> resp = new BasePageResp<>();
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

    public BasePageResp<GetCoinResp> publicApiGetCoinPair(BasePageReq<GetCoinReq> pageReq) {
        BasePageReq<GetCoinReqBO> page = toBasePageReqGetCoinReqBO(pageReq);

        BasePageResp<GetCoinRespBO> pageResp = getCoinPair(page);

        return toBasePageRespGetProductResp(pageResp);
    }

    private BasePageResp<GetCoinResp> toBasePageRespGetProductResp(BasePageResp<GetCoinRespBO> pageResp) {
        return null;
    }

    private BasePageResp<GetCoinRespBO> getCoinPair(BasePageReq<GetCoinReqBO> req) {
        Page<CoinPair> page = iCoinPairService.getCoins(req);
        return toPageGetCoinResp(page);
    }

    private BasePageReq<GetCoinReqBO> toBasePageReqGetCoinReqBO(BasePageReq<GetCoinReq> pageReq) {
        BasePageReq<GetCoinReqBO> target = new BasePageReq<>();
        target.setPageNum(pageReq.getPageNum());
        target.setIp(pageReq.getIp());
        target.setUserId(pageReq.getUserId());
        target.setPageSize(pageReq.getPageSize());
        target.setRequestId(pageReq.getRequestId());

        GetCoinReqBO query = new GetCoinReqBO();
        query.setRequestSource(EnumRequestSource.PUBLIC_API);
        target.setQuery(query);
        return target;
    }


    public GetCoinResp publicApiGetCoinPair(GetCoinReq req) {
        GetCoinReqBO reqBO = toGetCoinReqBO(req);

        GetCoinRespBO respBO = getCoinPair(reqBO);

        return toGetCoinResp(respBO);
    }

    private GetCoinRespBO getCoinPair(GetCoinReqBO reqBO) {
        CoinPair coinPair = iCoinPairService.getById(reqBO.getId());
        return toGetCoinRespBO(coinPair);
    }

    private GetCoinRespBO toGetCoinRespBO(CoinPair product) {
        GetCoinRespBO target = new GetCoinRespBO();
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

    private GetCoinReqBO toGetCoinReqBO(GetCoinReq req) {
        GetCoinReqBO reqBO = new GetCoinReqBO();
        reqBO.setId(req.getId());
        reqBO.setRequestSource(EnumRequestSource.PUBLIC_API);
        return reqBO;
    }

    private GetCoinResp toGetCoinResp(GetCoinRespBO source) {
        GetCoinResp target = new GetCoinResp();
        target.setId(source.getId());
        target.setBaseCurrency(source.getBaseCurrency());
        target.setBaseMaxQty(source.getBaseMaxQty());
        target.setBaseMinQty(source.getBaseMinQty());
        target.setBaseScale(source.getBaseScale());

        target.setQuoteCurrency(source.getQuoteCurrency());
        target.setQuoteScale(source.getQuoteScale());
        target.setBaseMaxQty(source.getQuoteMaxQty());
        target.setBaseMinQty(source.getQuoteMinQty());
        return target;
    }
}
