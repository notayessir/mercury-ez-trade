package com.notayessir.user.coin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.notayessir.common.vo.req.BasePageReq;
import com.notayessir.user.coin.bo.CheckCoinResultBO;
import com.notayessir.user.coin.bo.GetCoinReqBO;
import com.notayessir.user.coin.entity.CoinPair;
import com.baomidou.mybatisplus.extension.service.IService;
import com.notayessir.user.order.bo.CreateOrderReqBO;

public interface ICoinPairService extends IService<CoinPair> {


    void createCoin(CoinPair coinPair);

    Page<CoinPair> getCoins(BasePageReq<GetCoinReqBO> req);

    CheckCoinResultBO checkCoin(CreateOrderReqBO reqBO);


}
