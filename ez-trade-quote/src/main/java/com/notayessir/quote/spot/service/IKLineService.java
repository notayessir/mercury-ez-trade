package com.notayessir.quote.spot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.notayessir.engine.api.bo.MatchResultBO;
import com.notayessir.quote.spot.bo.GetKlineReqBO;
import com.notayessir.quote.spot.entity.KLine;

import java.util.List;

public interface IKLineService extends IService<KLine> {

    List<KLine> handleKLineUpdatedEvent(MatchResultBO event);

    List<KLine> findKline(GetKlineReqBO reqBO);

}
