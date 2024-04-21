package com.notayessir.quote.spot.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.notayessir.bo.MatchResultBO;
import com.notayessir.common.vo.req.BasePageReq;
import com.notayessir.quote.spot.bo.GetKlineReqBO;
import com.notayessir.quote.spot.entity.KLine;

import java.util.List;

public interface IKLineService extends IService<KLine> {

    List<KLine> handleKLineUpdatedEvent(MatchResultBO event);

    Page<KLine> findKline(BasePageReq<GetKlineReqBO> pageReq);

}
